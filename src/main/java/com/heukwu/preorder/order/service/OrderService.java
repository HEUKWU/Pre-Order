package com.heukwu.preorder.order.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.controller.dto.OrderRequestDto;
import com.heukwu.preorder.order.controller.dto.OrderResponseDto;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderProduct;
import com.heukwu.preorder.order.entity.OrderStatus;
import com.heukwu.preorder.order.repository.OrderProductRepository;
import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.entity.Stock;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.product.repository.StockRepository;
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
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public void orderProduct(OrderRequestDto requestDto, User user) {
        Product product = productRepository.findById(requestDto.productId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        OrderProduct orderProduct = createOrderProduct(product, requestDto.quantity());

        // 주문으로 인한 수량 감소
        int quantity = requestDto.quantity();
        Stock stock = stockRepository.findById(String.valueOf(product.getId())).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Not_FOUND_STOCK)
        );
        stock.decreaseQuantity(quantity);
        stockRepository.save(stock);

        Order order = Order.builder()
                .totalPrice(product.getPrice() * requestDto.quantity())
                .userId(user.getId())
                .orderProductList(List.of(orderProduct))
                .status(OrderStatus.CREATED)
                .build();

        orderRepository.save(order);
    }

    public List<OrderResponseDto> getUserOrderInfo(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());

        return orderList.stream().map(OrderResponseDto::of).toList();
    }

    @Transactional
    public OrderResponseDto orderWishlist(User user) {
        Wishlist wishlist = wishlistRepository.findById(user.getWishListId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_WISHLIST)
        );

        List<WishlistProduct> wishlistProducts = wishlist.getWishlistProducts();

        // 주문 생성
        return createOrder(user, wishlistProducts);
    }

    private OrderResponseDto createOrder(User user, List<WishlistProduct> wishlistProducts) {
        List<OrderProduct> orderProductList = new ArrayList<>();
        int totalPrice = 0;
        for (WishlistProduct wishlistProduct : wishlistProducts) {
            // 장바구니 상품
            Product product = productRepository.findById(wishlistProduct.getProductId()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT));

            totalPrice += wishlistProduct.getQuantity() * product.getPrice();

            // 주문에 따른 상품 수량 감소
            Stock stock = stockRepository.findById(String.valueOf(product.getId())).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Not_FOUND_STOCK)
            );
            stock.decreaseQuantity(wishlistProduct.getQuantity());
            stockRepository.save(stock);

            orderProductList.add(createOrderProduct(product, wishlistProduct.getQuantity()));

            // 장바구니 상품 삭제
            wishlistProduct.delete();
        }

        Order order = Order.builder()
                .totalPrice(totalPrice)
                .userId(user.getId())
                .orderProductList(orderProductList)
                .status(OrderStatus.CREATED)
                .build();

        return OrderResponseDto.of(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_ORDER)
        );

        if (order.isNotCancelableStatus()) {
            throw new BusinessException(ErrorMessage.CANNOT_CANCEL_ORDER);
        }

        order.cancelOrder();

        // 상품 재고 복구
        recoverProductQuantity(order);
    }

    @Transactional
    public void returnOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_ORDER)
        );

        if (order.isNotReturnableStatus()) {
            throw new BusinessException(ErrorMessage.CANNOT_RETURN_ORDER_STATUS);
        }
        if (order.isNotReturnableDate()) {
            throw new BusinessException(ErrorMessage.CANNOT_RETURN_ORDER_DATE);
        }

        order.returnOrder();
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
            order.updateStatus(OrderStatus.COMPLETED);
        }

        List<Order> returnedOrders = orderRepository.findAllByStatusAndModifiedAtBefore(OrderStatus.RETURNING, LocalDateTime.now().minusDays(1));

        // 반품 완료 후 하루 지난 상품 재고 복구
        for (Order order : returnedOrders) {
            recoverProductQuantity(order);

            order.updateStatus(OrderStatus.RETURNED);
        }
    }

    private void recoverProductQuantity(Order order) {
        List<OrderProduct> orderProductList = order.getOrderProductList();
        for (OrderProduct orderProduct : orderProductList) {
            Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
            );

            Stock stock = stockRepository.findById(String.valueOf(product.getId())).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Not_FOUND_STOCK)
            );
            stock.increaseQuantity(orderProduct.getQuantity());
            stockRepository.save(stock);
        }
    }

    private OrderProduct createOrderProduct(Product product, int quantity) {
        OrderProduct orderProduct = OrderProduct.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(quantity)
                .build();

        orderProductRepository.save(orderProduct);

        return orderProduct;
    }

}
