package com.heukwu.preorder.order.controller.dto;

import com.heukwu.preorder.order.entity.Order;
import lombok.Builder;

@Builder
public record OrderResponseDto(
        long userId,
        long productId,
        String productName,
        int quantity,
        int totalPrice
) {
        public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
                .userId(order.getUserId())
                .productId(order.getOrderProduct().getProductId())
                .productName(order.getOrderProduct().getName())
                .quantity(order.getOrderProduct().getQuantity())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
