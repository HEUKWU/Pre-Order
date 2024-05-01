package com.heukwu.preorder.common.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    WRONG_ID_PW("아이디와 비밀번호 확인이 필요합니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD("새로운 비밀번호는 이전 비밀번호와 같을 수 없습니다."),
    NOT_FOUND_EMAIL("이메일을 찾을 수 없습니다."),
    INCORRECT_CODE("인증번호가 일치하지 않습니다."),
    UNAUTHENTICATED_EMAIL("이메일 인증이 필요합니다."),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME("이미 사용중인 닉네임입니다."),
    DUPLICATE_EMAIL("이미 사용중인 이메일입니다."),
    TOKEN_ERROR("유효하지 않은 토큰입니다."),
    OUT_OF_STOCK_EXCEPTION("해당 상품은 품절되었습니다."),
    Not_FOUND_STOCK("해당 상룸의 재고를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("해당 상품을 찾을 수 없습니다."),
    NOT_FOUND_WISHLIST_PRODUCT("해당 장바구니 상품을 찾을 수 없습니다."),
    NOT_FOUND_WISHLIST("장바구니를 찾을 수 없습니다."),
    NOT_FOUND_ORDER("해당 주문 정보를 찾을 수 없습니다."),
    CANNOT_CANCEL_ORDER("배송중에는 주문 취소가 불가능합니다."),
    CANNOT_RETURN_ORDER_STATUS("배송이 완료되지 않은 상품은 반품이 불가능합니다."),
    CANNOT_RETURN_ORDER_DATE("배송완료 후 2일 이상 지난 상품은 반품이 불가능합니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
