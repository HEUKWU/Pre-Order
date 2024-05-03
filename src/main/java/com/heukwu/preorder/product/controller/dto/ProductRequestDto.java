package com.heukwu.preorder.product.controller.dto;

import lombok.Builder;

@Builder
public record ProductRequestDto(
        String name,
        String description,
        int price
) { }
