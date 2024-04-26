package com.heukwu.preorder.order.controller.dto;

import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderProduct;
import com.heukwu.preorder.order.entity.OrderStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponseDto(
        long userId,
        List<OrderProduct> orderProductList,
        int quantity,
        int totalPrice,
        OrderStatus orderStatus
) {
        public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
                .userId(order.getUserId())
                .orderProductList(order.getOrderProductList())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .build();
    }
}
