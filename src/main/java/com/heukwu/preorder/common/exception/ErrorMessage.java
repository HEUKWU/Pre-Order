package com.heukwu.preorder.common.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME("이미 사용중인 닉네임입니다."),
    DUPLICATE_EMAIL("이미 사용중인 이메일입니다."),
    TOKEN_ERROR("유효하지 않은 토큰입니다."),
    NOT_FOUND_PRODUCT("해당 상품을 찾을 수 없습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}