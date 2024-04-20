package com.heukwu.preorder.wishlist.entity;

import com.heukwu.preorder.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

// TODO : 상태를 둬서 soft detele.
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlistId")
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

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
}
