package com.heukwu.preorder.order.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.dto.OrderRequestDto;
import com.heukwu.preorder.order.dto.OrderResponseDto;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderStatus;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        // 장바구니 상품 처리
        orderInWishlist(user, product);

        Order order = Order.builder()
                .quantity(quantity)
                .totalPrice(product.getPrice() * requestDto.getQuantity())
                .user(user)
                .product(product)
                .status(OrderStatus.CREATED)
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

    @Transactional
    public List<OrderResponseDto> orderWishlist(User user) {
        Wishlist wishlist = wishlistRepository.findWishlistByUserId(user.getId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_WISHLIST)
        );

        List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

        // 주문 생성
        List<OrderResponseDto> orderResponseDtoList = createOrder(user, wishlistProducts);

        return orderResponseDtoList;
    }

    private List<OrderResponseDto> createOrder(User user, List<WishlistProduct> wishlistProducts) {
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (WishlistProduct wishlistProduct : wishlistProducts) {
            // 장바구니 상품
            Product product = wishlistProduct.getProduct();
            // 주문에 따른 상품 수량 감소
            product.decreaseQuantity(wishlistProduct.getQuantity());

            // 주문 생성
            Order order = Order.builder()
                    .quantity(wishlistProduct.getQuantity())
                    .totalPrice(wishlistProduct.getQuantity() * product.getPrice())
                    .user(user)
                    .product(product)
                    .status(OrderStatus.CREATED)
                    .build();

            orderRepository.save(order);
            // 장바구니 상품 삭제
            wishlistProductRepository.delete(wishlistProduct);
            orderResponseDtoList.add(OrderResponseDto.of(order));
        }

        return orderResponseDtoList;
    }

    // 주문 상태 변경
    @Transactional
    public void updateOrderStatus() {
        List<Order> createdOrders = orderRepository.findAllByStatusAndModifiedAtBefore(OrderStatus.CREATED, LocalDateTime.now().minusDays(1));

        // 생성 후 하루 지난 주문 배송중 처리
        for (Order order : createdOrders) {
            order.updateStatus(OrderStatus.SHIPPING);
        }

        List<Order> shippingOrders = orderRepository.findAllByStatusAndModifiedAtBefore(OrderStatus.SHIPPING, LocalDateTime.now().minusDays(1));

        // 배송 시작 후 하루 지난 주문 배송완료 처리
        for (Order order : shippingOrders) {
            order.updateStatus(OrderStatus.COMPLETE);
        }
    }
}
