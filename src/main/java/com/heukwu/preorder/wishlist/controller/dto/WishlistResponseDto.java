package com.heukwu.preorder.wishlist.controller.dto;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import lombok.Builder;

@Builder
public record WishlistResponseDto(
        long productId,
        int quantity
) {
    public static WishlistResponseDto of(WishlistProduct wishlistProduct) {
        return WishlistResponseDto.builder()
                .productId(wishlistProduct.getProduct().getId())
                .quantity(wishlistProduct.getQuantity())
                .build();
    }
}
