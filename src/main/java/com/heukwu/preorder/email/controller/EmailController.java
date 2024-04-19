package com.heukwu.preorder.email.controller;

import com.heukwu.preorder.common.SuccessMessage;
import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.email.dto.EmailRequestDto;
import com.heukwu.preorder.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseDto<String> sendVerificationEmail(@RequestBody @Validated EmailRequestDto.SendEmail requestDto) {
        emailService.sendVerificationEmail(requestDto);

        return ResponseDto.of(SuccessMessage.SEND_EMAIL.getMessage());
    }

    @PostMapping("/email/auth")
    public ResponseDto<String> verificationEmail(@RequestBody EmailRequestDto.Code requestDto) {
        emailService.verificationCode(requestDto);

        return ResponseDto.of(SuccessMessage.VERIFICATION_EMAIL.getMessage());
    }
}
