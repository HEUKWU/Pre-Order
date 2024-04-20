package com.heukwu.preorder.wishlist.repository;

import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {
    Optional<WishlistProduct> findWishlistProductByWishlistIdAndProductIdAndDeletedFalse(long wishlistId, long productId);
}
