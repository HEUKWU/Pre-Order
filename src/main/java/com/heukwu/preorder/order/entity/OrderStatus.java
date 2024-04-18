package com.heukwu.preorder.order.entity;

public enum OrderStatus {
    CREATED("생성됨"),
    SHIPPING("배송중"),
    COMPLETE("배송완료"),
    CANCELED("취소완료"),
    RETURNING("반품중"),
    RETURNED("반품완료"),
    ;

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
