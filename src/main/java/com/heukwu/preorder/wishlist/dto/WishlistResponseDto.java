package com.heukwu.preorder.wishlist.dto;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistResponseDto {

    private long userId;
    private long productId;

    public static WishlistResponseDto of(WishlistProduct wishlistProduct) {
        return WishlistResponseDto.builder()
                .userId(wishlistProduct.getWishlist().getUser().getId())
                .productId(wishlistProduct.getProduct().getId())
                .build();
    }
}
