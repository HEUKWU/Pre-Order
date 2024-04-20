package com.heukwu.preorder.email.controller.dto;

import jakarta.validation.constraints.Email;

public record EmailVerificationCodeRequestDto(
        @Email
        String email
) {}
