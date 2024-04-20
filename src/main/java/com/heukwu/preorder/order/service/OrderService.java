package com.heukwu.preorder.order.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.controller.dto.OrderRequestDto;
import com.heukwu.preorder.order.controller.dto.OrderResponseDto;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderStatus;
import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.wishlist.entity.Wishlist;
import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import com.heukwu.preorder.wishlist.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public OrderResponseDto orderProduct(OrderRequestDto requestDto, User user) {
        Product product = productRepository.findById(requestDto.productId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        // 주문으로 인한 수량 감소
        int quantity = requestDto.quantity();
        product.decreaseQuantity(quantity);

        Order order = Order.builder()
                .quantity(quantity)
                .totalPrice(product.getPrice() * requestDto.quantity())
                .userId(user.getId())
                .product(product)
                .status(OrderStatus.CREATED)
                .build();

        orderRepository.save(order);

        return OrderResponseDto.of(order);
    }

    public List<OrderResponseDto> getUserOrderInfo(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());

        return orderList.stream().map(OrderResponseDto::of).toList();
    }

    @Transactional
    public List<OrderResponseDto> orderWishlist(User user) {
        Wishlist wishlist = wishlistRepository.findById(user.getWishListId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_WISHLIST)
        );

        List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

        // 주문 생성
        return createOrder(user, wishlistProducts);
    }

    private List<OrderResponseDto> createOrder(User user, List<WishlistProduct> wishlistProducts) {
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (WishlistProduct wishlistProduct : wishlistProducts) {
            // 장바구니 상품
            Product product = productRepository.findById(wishlistProduct.getProductId()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT));

            // 주문에 따른 상품 수량 감소
            product.decreaseQuantity(wishlistProduct.getQuantity());

            // 주문 생성
            Order order = Order.builder()
                    .quantity(wishlistProduct.getQuantity())
                    .totalPrice(wishlistProduct.getQuantity() * product.getPrice())
                    .userId(user.getId())
                    .product(product)
                    .status(OrderStatus.CREATED)
                    .build();

            orderRepository.save(order);

            // 장바구니 상품 삭제
            wishlistProduct.delete();
            orderResponseDtoList.add(OrderResponseDto.of(order));
        }

        return orderResponseDtoList;
    }

    @Transactional
    public void cancelOrder(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());

        for (Order order : orderList) {
            // 배송이 시작되었다면 취소 불가
            if (order.isNotCancelable()) {
                throw new BusinessException(ErrorMessage.CANNOT_CANCEL_ORDER);
            }

            order.cancelOrder();

            // 상품 재고 복구
            Product product = order.getProduct();
            product.increaseQuantity(order.getQuantity());
        }
    }

    @Transactional
    public void returnOrder(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());

        long notReturnableCount = orderList.stream()
                .filter(Order::isNotReturnable)
                .count();

        if (notReturnableCount > 0) {
            throw new BusinessException(ErrorMessage.CANNOT_RETURN_ORDER);
        }

        orderList.forEach(Order::returnOrder);
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

        List<Order> returnedOrders = orderRepository.findAllByStatusAndModifiedAtBefore(OrderStatus.RETURNING, LocalDateTime.now().minusDays(1));

        // 반품 완료 후 하루 지난 상품 재고 복구
        for (Order order : returnedOrders) {
            Product product = order.getProduct();

            product.increaseQuantity(order.getQuantity());
            order.updateStatus(OrderStatus.RETURNED);
        }
    }
}
