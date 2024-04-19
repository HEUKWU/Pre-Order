package com.heukwu.preorder.email.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.common.util.EncryptUtil;
import com.heukwu.preorder.email.dto.EmailRequestDto;
import com.heukwu.preorder.email.dto.EmailResponseDto;
import com.heukwu.preorder.email.entity.Email;
import com.heukwu.preorder.email.repository.EmailRepository;
import com.heukwu.preorder.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final EncryptUtil encryptor;
    private final JavaMailSender mailSender;

    @Transactional
    public void sendVerificationEmail(EmailRequestDto.SendEmail requestDto) {
        checkEmailDuplicate(requestDto.getEmail());

        String code = createCode();
        sendEmail(requestDto.getEmail(), code);

        Email email = Email.builder().email(encryptor.encrypt(requestDto.getEmail())).code(encryptor.encrypt(code)).build();
        emailRepository.save(email);
    }

    private void checkEmailDuplicate(String email) {
        String encryptedEmail = encryptor.encrypt(email);

        if (userRepository.findUserByEmail(encryptedEmail).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }
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

    public void sendEmail(String email, String code) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setSubject("회원가입 인증번호");
            helper.setTo(email);
            helper.setText(code, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    public void verificationCode(EmailRequestDto.Code requestDto) {
        String encryptedEmail = encryptor.encrypt(requestDto.getEmail());

        Email email = emailRepository.findEmailByEmail(encryptedEmail).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_EMAIL)
        );

        String code = encryptor.decrypt(email.getCode());

        if (!requestDto.getCode().equals(code)) {
            throw new BusinessException(ErrorMessage.INCORRECT_CODE);
        }
    }
}
