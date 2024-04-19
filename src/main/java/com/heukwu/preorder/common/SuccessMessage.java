package com.heukwu.preorder.common;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    SEND_EMAIL("메일이 전송되었습니다."),
    VERIFICATION_EMAIL("이메일 인증이 완료되었습니다."),
    SIGNUP("회원가입이 완료되었습니다."),
    LOGIN("로그인이 완료되었습니다."),
    UPDATE_ADDRESS("주소 변경이 완료되었습니다."),
    UPDATE_PHONE_NUMBER("전화번호 변경이 완료되었습니다."),
    UPDATE_PASSWORD("비밀번호 변경이 완료되었습니다."),
    CANCEL_ORDER("주문이 취소되었습니다."),
    RETURN_ORDER("반품 처리되었습니다."),
    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
