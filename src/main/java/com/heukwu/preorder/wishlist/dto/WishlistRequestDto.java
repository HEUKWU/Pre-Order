package com.heukwu.preorder.wishlist.dto;

import lombok.Builder;
import lombok.Getter;

public class WishlistRequestDto {

    @Getter
    @Builder
    public static class Create {
        private long productId;
        private int quantity;
    }

    @Getter
    @Builder
    public static class Update {
        private long wishlistProductId;
        private int quantity;
    }
}


