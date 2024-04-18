package com.heukwu.preorder.wishlist.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.wishlist.dto.WishlistResponseDto;
import com.heukwu.preorder.wishlist.entity.Wishlist;
import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import com.heukwu.preorder.wishlist.repository.WishlistProductRepository;
import com.heukwu.preorder.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final ProductRepository productRepository;
    private final WishlistProductRepository wishlistProductRepository;
    private final WishlistRepository wishlistRepository;

    public WishlistResponseDto addWishlist(Long productId, User user) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        Wishlist wishlist = getWishlist(user);

        WishlistProduct wishlistProduct = WishlistProduct.builder()
                .wishlist(wishlist)
                .product(product)
                .build();

        wishlistProductRepository.save(wishlistProduct);

        return WishlistResponseDto.of(wishlistProduct);
    }

    private Wishlist getWishlist(User user) {
        Optional<Wishlist> optionalWishlist = wishlistRepository.findWishlistByUserId(user.getId());

        Wishlist wishlist;
        if (optionalWishlist.isPresent()) {
            wishlist = optionalWishlist.get();
        } else {
            wishlist = Wishlist.builder().user(user).build();

            wishlistRepository.save(wishlist);
        }

        return wishlist;
    }
}
