package com.heukwu.preorder.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

//    private final JavaMailSender mailSender;
//
//    @Async
//    public void sendVerificationEmail(String email) {
//        String token = generateVerificationToken();
//        String subject = "이메일 인증";
//        String text =
//    }
//
//    private String generateVerificationToken() {
//        return UUID.randomUUID().toString();
//    }
}
