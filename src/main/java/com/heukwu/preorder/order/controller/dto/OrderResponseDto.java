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
                .userId(order.getUser().getId())
                .productId(order.getProduct().getId())
                .productName(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
