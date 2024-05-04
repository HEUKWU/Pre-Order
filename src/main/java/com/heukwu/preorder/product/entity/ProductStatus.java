package com.heukwu.preorder.product.entity;

public enum ProductStatus {
    ON_SALE("판매중"),
    PENDING("판매 대기"),
    ;

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }
}
