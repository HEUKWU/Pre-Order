package com.heukwu.preorder.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class Signup {

        private String username;

        @Pattern(regexp = "(?=.*?[a-zA-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+=\\-`]).{8,30}")
        private String password;

        @Email
        private String email;

        private String address;

        private String phoneNumber;
    }
}
