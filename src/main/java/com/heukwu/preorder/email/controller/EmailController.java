package com.heukwu.preorder.email.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.email.dto.EmailRequestDto;
import com.heukwu.preorder.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseDto<Boolean> sendVerificationEmail(@RequestBody @Valid EmailRequestDto.SendEmail requestDto) {
        emailService.sendVerificationEmail(requestDto);

        return ResponseDto.success();
    }

    @PostMapping("/email/auth")
    public ResponseDto<Boolean> verificationEmail(@RequestBody EmailRequestDto.Code requestDto) {
        emailService.verificationCode(requestDto);

        return ResponseDto.success();
    }
}
