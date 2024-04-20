package com.heukwu.preorder.wishlist.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlistId")
    private Wishlist wishlist;

    private Long productId;

    private int quantity;

    @Column
    private boolean isDeleted = Boolean.FALSE;

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void delete() {
        this.isDeleted = true;
    }

    @Builder
    public WishlistProduct(Long id, Wishlist wishlist, Long productId, int quantity) {
        this.id = id;
        this.wishlist = wishlist;
        this.productId = productId;
        this.quantity = quantity;
    }
}
