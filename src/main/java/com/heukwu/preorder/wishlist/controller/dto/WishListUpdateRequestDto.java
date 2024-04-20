package com.heukwu.preorder.wishlist.controller.dto;

import lombok.Builder;

@Builder
public record WishListUpdateRequestDto(
        long wishlistProductId,
        int quantity
) { }
