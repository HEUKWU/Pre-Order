package com.heukwu.preorder.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderRequestDto {
    private long productId;
    private int quantity;
}
