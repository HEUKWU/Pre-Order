package com.heukwu.preorder.product.controller.dto;

import lombok.Builder;

@Builder
public record ProductStockResponseDto(
        long productId,
        int quantity
) {
    public static ProductStockResponseDto of(long productId, int quantity) {
        return ProductStockResponseDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
