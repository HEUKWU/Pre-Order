package com.heukwu.preorder.wishlist.controller.dto;

import lombok.Builder;

@Builder
public record WishListAddRequestDto(
        long productId,
        int quantity
) { }
