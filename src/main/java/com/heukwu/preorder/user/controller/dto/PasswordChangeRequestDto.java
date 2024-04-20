package com.heukwu.preorder.user.controller.dto;

import jakarta.validation.constraints.Pattern;

public record PasswordChangeRequestDto(

        @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+=\\-`]).{8,30}")
        String password,

        @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+=\\-`]).{8,30}")
        String newPassword
) { }
