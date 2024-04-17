package com.heukwu.preorder.exception;

import lombok.Builder;

@Builder
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
