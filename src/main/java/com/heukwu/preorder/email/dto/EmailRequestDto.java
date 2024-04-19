package com.heukwu.preorder.email.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailRequestDto {

    @Getter
    public static class SendEmail {
        @Email
        private String email;
    }

    @Getter
    public static class Code {
        private String email;
        private String code;
    }
}
