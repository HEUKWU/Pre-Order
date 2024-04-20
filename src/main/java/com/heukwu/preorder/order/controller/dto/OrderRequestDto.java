package com.heukwu.preorder.order.controller.dto;

import lombok.Builder;

@Builder
public record OrderRequestDto (
        long productId,
        int quantity
) { }
