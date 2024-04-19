package com.heukwu.preorder.wishlist.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.wishlist.dto.WishlistRequestDto;
import com.heukwu.preorder.wishlist.dto.WishlistResponseDto;
import com.heukwu.preorder.wishlist.entity.Wishlist;
import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import com.heukwu.preorder.wishlist.repository.WishlistProductRepository;
import com.heukwu.preorder.wishlist.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final ProductRepository productRepository;
    private final WishlistProductRepository wishlistProductRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public WishlistResponseDto addWishlist(WishlistRequestDto.Create requestDto, User user) {
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        Wishlist wishlist = getWishlist(user);

        WishlistProduct wishlistProduct = createWishlistProduct(requestDto, wishlist, product);

        return WishlistResponseDto.of(wishlistProduct);
    }

    public List<WishlistResponseDto> getUserWishlist(User user) {
        Wishlist wishlist = getWishlist(user);
        List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

        List<WishlistResponseDto> wishlistResponseDtoList = wishlistProducts.stream().map(WishlistResponseDto::of).toList();

        return wishlistResponseDtoList;
    }

    @Transactional
    public WishlistResponseDto updateWishlist(WishlistRequestDto.Update requestDto) {
        WishlistProduct wishlistProduct = wishlistProductRepository.findById(requestDto.getWishlistProductId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_WISHLIST_PRODUCT)
        );

        wishlistProduct.updateQuantity(requestDto.getQuantity());

        return WishlistResponseDto.of(wishlistProduct);
    }

    private Wishlist getWishlist(User user) {
        Optional<Wishlist> optionalWishlist = wishlistRepository.findWishlistByUserId(user.getId());

        Wishlist wishlist;
        // 장바구니가 이미 존재한다면
        if (optionalWishlist.isPresent()) {
            // 그대로 반환
            wishlist = optionalWishlist.get();
        } else {
            // 없다면 만들어서 반환
            wishlist = Wishlist.builder().user(user).build();
            wishlistRepository.save(wishlist);
        }

        return wishlist;
    }

    private WishlistProduct createWishlistProduct(WishlistRequestDto.Create requestDto, Wishlist wishlist, Product product) {
        Optional<WishlistProduct> optionalWishlistProduct = wishlistProductRepository.findWishlistProductByWishlistIdAndProductId(wishlist.getId(), product.getId());

        WishlistProduct wishlistProduct;
        // 해당 상품이 이미 장바구니에 있다면
        if (optionalWishlistProduct.isPresent()) {
            wishlistProduct = optionalWishlistProduct.get();
            // 수량 업데이트
            wishlistProduct.increaseQuantity(requestDto.getQuantity());
        } else {
            wishlistProduct = WishlistProduct.builder()
                    .wishlist(wishlist)
                    .product(product)
                    .quantity(requestDto.getQuantity())
                    .build();

            wishlistProductRepository.save(wishlistProduct);
        }

        return wishlistProduct;
    }
}
