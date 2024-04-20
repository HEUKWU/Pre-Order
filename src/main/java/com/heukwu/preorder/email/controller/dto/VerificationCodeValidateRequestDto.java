package com.heukwu.preorder.email.controller.dto;

public record VerificationCodeValidateRequestDto(
        String email,
        String code
) { }
