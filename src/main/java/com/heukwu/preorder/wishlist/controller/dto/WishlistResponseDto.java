package com.heukwu.preorder.wishlist.controller.dto;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import lombok.Builder;

@Builder
public record WishlistResponseDto(
        long userId,
        long productId,
        int quantity,
        String productPageUrl
) {
    public static WishlistResponseDto of(WishlistProduct wishlistProduct) {
        return WishlistResponseDto.builder()
                .userId(wishlistProduct.getWishlist().getUser().getId())
                .productId(wishlistProduct.getProduct().getId())
                .quantity(wishlistProduct.getQuantity())
                .build();
    }
}
