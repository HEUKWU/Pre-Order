package com.heukwu.preorder.product.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.heukwu.preorder.product.entity.Product;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDto(
        long id,
        String name,
        String description,
        int price
) {
    public static ProductResponseDto toListResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
