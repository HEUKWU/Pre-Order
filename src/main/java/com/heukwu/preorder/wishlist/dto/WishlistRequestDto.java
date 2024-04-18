package com.heukwu.preorder.wishlist.dto;

import lombok.Getter;

public class WishlistRequestDto {

    @Getter
    public static class Create {
        private long productId;
        private int quantity;
    }

    @Getter
    public static class Update {
        private long wishlistProductId;
        private int quantity;
    }
}


