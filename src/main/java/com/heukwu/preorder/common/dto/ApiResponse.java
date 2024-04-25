package com.heukwu.preorder.common.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int code,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data);
    }

    public static ApiResponse<Boolean> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), true);
    }

    public static ApiResponse<String> fail(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.value(), message);
    }
}
