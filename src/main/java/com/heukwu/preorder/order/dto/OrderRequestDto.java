package com.heukwu.preorder.order.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {
    private long productId;
    private int quantity;
}
