package com.heukwu.preorder.order.entity;

public enum OrderStatus {
    CREATED("생성됨"),
    PAYING("결제중"),
    COMPLETED_PAYMENT("결제완료"),
    SHIPPING("배송중"),
    COMPLETED_SHIPMENT("배송완료"),
    CANCELED("취소완료"),
    RETURNING("반품중"),
    RETURNED("반품완료"),
    ;

    OrderStatus(String status) {
    }
}
