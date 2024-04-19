package com.heukwu.preorder.common.dto;

import org.springframework.http.HttpStatus;

public record ResponseDto<T>(
        int code,
        T data
) {
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(HttpStatus.OK.value(), data);
    }

    public static ResponseDto<Boolean> success() {
        return new ResponseDto<>(HttpStatus.OK.value(), true);
    }
}
