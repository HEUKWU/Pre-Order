package com.heukwu.preorder.email.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.email.controller.dto.EmailVerificationCodeRequestDto;
import com.heukwu.preorder.email.controller.dto.VerificationCodeValidateRequestDto;
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
    public ApiResponse<Boolean> sendVerificationEmail(@RequestBody @Valid EmailVerificationCodeRequestDto requestDto) {
        emailService.sendVerificationEmail(requestDto);

        return ApiResponse.success();
    }

    @PostMapping("/email/auth")
    public ApiResponse<Boolean> verificationEmail(@RequestBody VerificationCodeValidateRequestDto requestDto) {
        emailService.verificationCode(requestDto);

        return ApiResponse.success();
    }
}
