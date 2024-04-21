package com.heukwu.preorder.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequestDto(

        @NotBlank(message = "아이디를 입력해주세요")
        String username,

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+=\\-`]).{8,15}",
                message = "비밀번호는 최소한 하나의 숫자, 문자, 특수문자 ~!@#$%^&*()_+=-`를 포함한 8글자 ~ 15글자여야 합니다.")
        String password,

        @NotBlank(message = "이름을 입력해주세요")
        String name,

        @Email(message = "이메일 형식에 맞춰서 입력해주세요")
        String email,

        @NotBlank
        String address,

        @NotBlank
        String phoneNumber
) { }
