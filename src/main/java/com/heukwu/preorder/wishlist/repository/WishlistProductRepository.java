package com.heukwu.preorder.wishlist.repository;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {
}
