package com.heukwu.preorder.email.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.email.controller.dto.EmailVerificationCodeRequestDto;
import com.heukwu.preorder.email.controller.dto.VerificationCodeValidateRequestDto;
import com.heukwu.preorder.email.entity.EmailVerificationHistory;
import com.heukwu.preorder.email.entity.EmailVerificationStatusEnum;
import com.heukwu.preorder.email.repository.EmailRepository;
import com.heukwu.preorder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EmailSender emailSender;

    public void sendVerificationEmail(EmailVerificationCodeRequestDto requestDto) {
        checkEmailDuplicate(requestDto.email());

        EmailVerificationHistory emailVerificationHistory = getEmailVerificationHistory(requestDto.email());
        emailSender.sendEmail(requestDto.email(),emailVerificationHistory.getCode());

        emailRepository.save(emailVerificationHistory);
    }

    private void checkEmailDuplicate(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }
    }

    private EmailVerificationHistory getEmailVerificationHistory(String email) {
        Optional<EmailVerificationHistory> optionalEmailVerificationHistory = emailRepository.findByEmailAndVerificationStatus(email, EmailVerificationStatusEnum.CREATED);

        return optionalEmailVerificationHistory.orElseGet(() -> EmailVerificationHistory.builder()
                .email(email)
                .code(createCode())
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

    @Transactional
    public void verificationCode(VerificationCodeValidateRequestDto requestDto) {
        EmailVerificationHistory emailVerificationHistory = emailRepository.findEmailByEmail(requestDto.email()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_EMAIL)
        );

        if (!requestDto.code().equals(emailVerificationHistory.getCode())) {
            throw new BusinessException(ErrorMessage.INCORRECT_CODE);
        }

        emailVerificationHistory.verified();
    }
}
