package com.heukwu.preorder.order.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.controller.dto.OrderRequestDto;
import com.heukwu.preorder.order.controller.dto.OrderResponseDto;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderProduct;
import com.heukwu.preorder.order.entity.OrderStatus;
import com.heukwu.preorder.order.repository.OrderProductRepository;
import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.wishlist.entity.Wishlist;
import com.heukwu.preorder.wishlist.entity.WishlistProduct;
import com.heukwu.preorder.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishlistRepository wishlistRepository;

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
        orderService.orderProduct(requestDto, user);

        //then
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
        OrderProduct orderProduct1 = OrderProduct.builder().id(1L).productId(product.getId()).build();
        OrderProduct orderProduct2 = OrderProduct.builder().id(1L).productId(product.getId()).build();
        OrderProduct orderProduct3 = OrderProduct.builder().id(1L).productId(product.getId()).build();
        Order order1 = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct1, orderProduct2, orderProduct3)).totalPrice(1000).build();
        Order order2 = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct1, orderProduct2, orderProduct3)).totalPrice(1000).build();
        Order order3 = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct1, orderProduct2, orderProduct3)).totalPrice(1000).build();

        when(orderRepository.findAllByUserId(1L)).thenReturn(List.of(order1, order2, order3));

        //when
        List<OrderResponseDto> result = orderService.getUserOrderInfo(user);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("장바구니 상품 일괄 주문시 반환 리스트의 개수는 장바구니 상품의 개수와 같다.")
    public void orderWishlist() {
        //given
        Wishlist wishlist = Wishlist.builder().id(1L).build();
        User user = User.builder().id(1L).wishListId(wishlist.getId()).build();
        Product product = Product.builder().id(1L).build();
        WishlistProduct wishlistProduct1 = WishlistProduct.builder().id(1L).wishlist(wishlist).productId(product.getId()).quantity(1).build();
        WishlistProduct wishlistProduct2 = WishlistProduct.builder().id(2L).wishlist(wishlist).productId(product.getId()).quantity(1).build();
        wishlist = Wishlist.builder().wishlistProducts(List.of(wishlistProduct1, wishlistProduct2)).build();

        when(wishlistRepository.findById(user.getWishListId())).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //when
        OrderResponseDto result = orderService.orderWishlist(user);

        //then
        assertThat(result.orderProductList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문취소가 완료되면 주문의 상태가 취소됨으로 변경된다.")
    public void cancelOrder() {
        //given
        User user = User.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        OrderProduct orderProduct = OrderProduct.builder().id(1L).productId(product.getId()).build();
        Order order = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct)).totalPrice(1000).status(OrderStatus.CREATED).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        //when
        orderService.cancelOrder(1L);

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("배송이 시작되었다면 주문 취소가 불가능하다.")
    public void cannotCancelOrder() {
        //given
        User user = User.builder().id(1L).build();
        OrderProduct orderProduct = OrderProduct.builder().id(1L).productId(1L).build();
        Order order = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct)).totalPrice(1000).status(OrderStatus.SHIPPING).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when, then
        assertThatThrownBy(() -> orderService.cancelOrder(1L)).isInstanceOf(BusinessException.class).hasMessage("배송중에는 주문 취소가 불가능합니다.");
    }

    @Test
    @DisplayName("반품 신청이 완료되면 주문 상태가 반품중으로 변경된다.")
    public void returnOrder() {
        //given
        User user = User.builder().id(1L).build();
        OrderProduct orderProduct = OrderProduct.builder().id(1L).productId(1L).build();
        Order order = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct)).totalPrice(1000).status(OrderStatus.COMPLETED).modifiedAt(LocalDate.now()).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when
        orderService.returnOrder(1L);

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.RETURNING);
    }

    @Test
    @DisplayName("배송이 완료되지 않은 주문은 반품이 불가능하다.")
    public void cannotReturnOrderStatus() {
        //given
        User user = User.builder().id(1L).build();
        OrderProduct orderProduct = OrderProduct.builder().id(1L).productId(1L).build();
        Order order = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct)).totalPrice(1000).status(OrderStatus.SHIPPING).modifiedAt(LocalDate.now()).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when, then
        assertThatThrownBy(() -> orderService.returnOrder(1L)).isInstanceOf(BusinessException.class).hasMessage("배송이 완료되지 않은 상품은 반품이 불가능합니다.");
    }

    @Test
    @DisplayName("배송완료 후 2일 이상 지난 주문은 반품이 불가능하다.")
    public void cannotReturnOrderDate() {
        //given
        User user = User.builder().id(1L).build();
        OrderProduct orderProduct = OrderProduct.builder().id(1L).productId(1L).build();
        Order order = Order.builder().userId(user.getId()).orderProductList(List.of(orderProduct)).totalPrice(1000).status(OrderStatus.COMPLETED).modifiedAt(LocalDate.now().minusDays(2)).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when, then
        assertThatThrownBy(() -> orderService.returnOrder(1L)).isInstanceOf(BusinessException.class).hasMessage("배송완료 후 2일 이상 지난 상품은 반품이 불가능합니다.");
    }
}
