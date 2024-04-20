package com.heukwu.preorder.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequestDto(

        @NotBlank
        String username,

        @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+=\\-`]).{8,30}")
        String password,

        @NotBlank
        String name,

        @Email
        String email,

        @NotBlank
        String address,

        @NotBlank
        String phoneNumber
) { }
