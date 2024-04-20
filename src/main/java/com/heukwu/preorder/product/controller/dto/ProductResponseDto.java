package com.heukwu.preorder.product.controller.dto;

import com.heukwu.preorder.product.entity.Product;
import lombok.Builder;

@Builder
public record ProductResponseDto(
        long id,
        String name,
        String description,
        int price,
        int quantity
) {
    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
