package com.heukwu.preorder.email.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.common.util.EncryptUtil;
import com.heukwu.preorder.email.dto.EmailRequestDto;
import com.heukwu.preorder.email.entity.EmailVerificationHistory;
import com.heukwu.preorder.email.entity.EmailVerificationStatusEnum;
import com.heukwu.preorder.email.repository.EmailRepository;
import com.heukwu.preorder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final EncryptUtil encryptor;
    private final EmailSender emailSender;

    public void sendVerificationEmail(EmailRequestDto.SendEmail requestDto) {
        checkEmailDuplicate(requestDto.getEmail());

        EmailVerificationHistory emailVerificationHistory = getEmailVerificationHistory(requestDto.getEmail());
        emailSender.sendEmail(requestDto.getEmail(), encryptor.decrypt(emailVerificationHistory.getCode()));

        emailRepository.save(emailVerificationHistory);
    }

    private void checkEmailDuplicate(String email) {
        String encryptedEmail = encryptor.encrypt(email);

        if (userRepository.findUserByEmail(encryptedEmail).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }
    }

    private EmailVerificationHistory getEmailVerificationHistory(String email) {
        Optional<EmailVerificationHistory> optionalEmailVerificationHistory = emailRepository.findByEmailAndVerificationStatus(encryptor.encrypt(email), EmailVerificationStatusEnum.CREATED);

        return optionalEmailVerificationHistory.orElseGet(() -> EmailVerificationHistory.builder()
                .email(encryptor.encrypt(email))
                .code(encryptor.encrypt(createCode()))
                .verificationStatus(EmailVerificationStatusEnum.CREATED)
                .build());

    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public void verificationCode(EmailRequestDto.Code requestDto) {
        String encryptedEmail = encryptor.encrypt(requestDto.getEmail());

        EmailVerificationHistory emailVerificationHistory = emailRepository.findEmailByEmail(encryptedEmail).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_EMAIL)
        );

        String code = encryptor.decrypt(emailVerificationHistory.getCode());

        if (!requestDto.getCode().equals(code)) {
            throw new BusinessException(ErrorMessage.INCORRECT_CODE);
        }

        emailVerificationHistory.verified();
    }
}
