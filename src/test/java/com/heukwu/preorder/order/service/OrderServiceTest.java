package com.heukwu.preorder.order.service;

import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.wishlist.repository.WishlistProductRepository;
import com.heukwu.preorder.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistProductRepository wishlistProductRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder() {
        Product product = Product.builder().id(1L).name("Product").description("Description").price(1000).quantity(10).build();

    }
}
