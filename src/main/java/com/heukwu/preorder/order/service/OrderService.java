package com.heukwu.preorder.order.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.dto.OrderRequestDto;
import com.heukwu.preorder.order.dto.OrderResponseDto;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final WishlistProductRepository wishlistProductRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public OrderResponseDto orderProduct(OrderRequestDto requestDto, User user) {
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        // 주문으로 인한 수량 감소
        int quantity = requestDto.getQuantity();
        product.decreaseQuantity(quantity);

        // 장바구니 주문
        orderInWishlist(user, product);

        Order order = Order.builder()
                .quantity(quantity)
                .totalPrice(product.getPrice() * requestDto.getQuantity())
                .user(user)
                .product(product)
                .build();

        orderRepository.save(order);

        return OrderResponseDto.of(order);
    }

    private void orderInWishlist(User user, Product product) {
        Optional<Wishlist> optionalWishlist = wishlistRepository.findWishlistByUserId(user.getId());
        if (optionalWishlist.isPresent()) {
            Wishlist wishlist = optionalWishlist.get();
            Optional<WishlistProduct> optionalWishlistProduct = wishlistProductRepository.findWishlistProductByWishlistIdAndProductId(wishlist.getId(), product.getId());

            // 장바구니에 있는 상품 삭제 처리
            if (optionalWishlistProduct.isPresent()) {
                WishlistProduct wishlistProduct = optionalWishlistProduct.get();
                wishlistProductRepository.delete(wishlistProduct);
            }
        }
    }

    public List<OrderResponseDto> getUserOrderInfo(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        List<OrderResponseDto> orderResposeDtoList = orderList.stream().map(OrderResponseDto::of).toList();

        return orderResposeDtoList;
    }
}
