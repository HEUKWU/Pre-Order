package com.heukwu.preorder.user.dto;

import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class Signup {
        private String username;
        private String password;
        private String email;
        private String address;
        private String phoneNumber;
    }
}
