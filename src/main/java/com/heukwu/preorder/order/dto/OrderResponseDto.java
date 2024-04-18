package com.heukwu.preorder.order.dto;

import com.heukwu.preorder.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponseDto {

    private long userId;
    private long productId;
    private String productName;
    private int quantity;
    private int totalPrice;

    public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
                .userId(order.getUser().getId())
                .productId(order.getProduct().getId())
                .productName(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
