package com.heukwu.preorder.product.controller.dto;

public record ProductStockRequestDto(
        long productId,
        int quantity
) { }
