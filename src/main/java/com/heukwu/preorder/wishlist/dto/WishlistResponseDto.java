package com.heukwu.preorder.wishlist.dto;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistResponseDto {

    private long wishlistProductId;
    private long userId;
    private long productId;
    private int quantity;
    private String productPageUrl;

    public static WishlistResponseDto of(WishlistProduct wishlistProduct) {
        Long productId = wishlistProduct.getProduct().getId();

        return WishlistResponseDto.builder()
                .wishlistProductId(wishlistProduct.getId())
                .userId(wishlistProduct.getWishlist().getUser().getId())
                .productId(productId)
                .quantity(wishlistProduct.getQuantity())
                .productPageUrl("/product/" + productId)
                .build();
    }
}
