package com.heukwu.preorder.order.service;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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
    @DisplayName("상품 주문시 결과값의 총 금액은 상품의 금액 * 구매 수량의 값과 같다. 구매한 상품의 개수만큼 재고가 감소한다.")
    public void createOrder() {
        //given
        User user = User.builder().id(1L).build();
        Product product = Product.builder().id(1L).name("Product").description("Description").price(1000).quantity(10).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderRequestDto requestDto = OrderRequestDto.builder().productId(1L).quantity(1).build();

        //when
        OrderResponseDto result = orderService.orderProduct(requestDto, user);

        //then
        assertThat(result.getTotalPrice()).isEqualTo(product.getPrice() * requestDto.getQuantity());
        assertThat(product.getQuantity()).isEqualTo(9);
    }

    @Test
    @DisplayName("잘못된 상품의 id로 주문시 예외가 발생한다.")
    public void createOrderNotExistProduct() {
        //given
        User user = User.builder().id(1L).build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        OrderRequestDto requestDto = OrderRequestDto.builder().productId(1L).quantity(1).build();

        //when, then
        assertThatThrownBy(() -> orderService.orderProduct(requestDto, user)).isInstanceOf(NotFoundException.class).hasMessage("해당 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 정보 조회시 반환 리스트의 개수는 사용자의 주문 개수와 같다.")
    public void getOrderInfo() {
        //given
        User user = User.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        Order order1 = Order.builder().user(user).product(product).quantity(1).totalPrice(1000).build();
        Order order2 = Order.builder().user(user).product(product).quantity(1).totalPrice(1000).build();
        Order order3 = Order.builder().user(user).product(product).quantity(1).totalPrice(1000).build();

        List<Order> orders = List.of(order1, order2, order3);

        when(orderRepository.findAllByUserId(user.getId())).thenReturn(orders);

        //when
        List<OrderResponseDto> result = orderService.getUserOrderInfo(user);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("장바구니 상품 일괄 주문시 반환 리스트의 개수는 장바구니 상품의 개수와 같다.")
    public void orderWishlist() {
        //given
        User user = User.builder().id(1L).build();
        Wishlist wishlist = Wishlist.builder().id(1L).user(user).build();
        Product product = Product.builder().id(1L).build();
        WishlistProduct wishlistProduct1 = WishlistProduct.builder().id(1L).wishlist(wishlist).product(product).quantity(1).build();
        WishlistProduct wishlistProduct2 = WishlistProduct.builder().id(2L).wishlist(wishlist).product(product).quantity(1).build();
        wishlist = Wishlist.builder().wishlistProducts(List.of(wishlistProduct1, wishlistProduct2)).build();

        when(wishlistRepository.findWishlistByUserId(user.getId())).thenReturn(Optional.of(wishlist));

        //when
        List<OrderResponseDto> result = orderService.orderWishlist(user);

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}
