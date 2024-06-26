package com.heukwu.preorder.wishlist.service;

import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.wishlist.controller.dto.WishListAddRequestDto;
import com.heukwu.preorder.wishlist.controller.dto.WishListUpdateRequestDto;
import com.heukwu.preorder.wishlist.controller.dto.WishlistResponseDto;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistProductRepository wishlistProductRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @Test
    @DisplayName("장바구니에 상품을 저장했을시 결과의 상품 아이디, 수량은 저장한 상품의 아이디, 수량과 같다.")
    public void testAddWishlist() {
        //given
        WishListAddRequestDto requestDto = WishListAddRequestDto.builder().productId(1L).quantity(1).build();
        Wishlist wishlist = Wishlist.builder().id(1L).build();
        User user = User.builder().id(1L).wishListId(wishlist.getId()).build();
        when(wishlistRepository.findById(user.getWishListId())).thenReturn(Optional.of(wishlist));

        Product product = Product.builder().id(1L).name("Product").description("Description").price(1000).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // 장바구니에 해당 상품이 이미 한개 저장되어있음
        when(wishlistProductRepository.findWishlistProductByWishlistIdAndProductIdAndDeletedFalse(wishlist.getId(), product.getId())).thenReturn(Optional.empty());

        //when
        WishlistResponseDto result = wishlistService.addWishlist(requestDto, user);

        //then
        assertThat(result.productId()).isEqualTo(1L);
        assertThat(result.quantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니에 해당 상품이 이미 있다면 장바구니에 저장된 상품 수량이 다시 저장하려는 수량만큼 증가한다.")
    public void testAddWishlistAgain() {
        //given
        // 장바구니에 한개의 상품 저장
        WishListAddRequestDto requestDto = WishListAddRequestDto.builder().productId(1L).quantity(1).build();
        Wishlist wishlist = Wishlist.builder().id(1L).build();
        User user = User.builder().id(1L).wishListId(wishlist.getId()).build();

        when(wishlistRepository.findById(user.getWishListId())).thenReturn(Optional.of(wishlist));

        Product product = Product.builder().id(1L).name("Product").description("Description").price(1000).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // 장바구니에 해당 상품이 이미 한개 저장되어있음
        WishlistProduct wishlistProduct = WishlistProduct.builder().id(1L).wishlist(wishlist).productId(product.getId()).quantity(1).build();
        when(wishlistProductRepository.findWishlistProductByWishlistIdAndProductIdAndDeletedFalse(wishlist.getId(), product.getId())).thenReturn(Optional.of(wishlistProduct));

        //when
        WishlistResponseDto result = wishlistService.addWishlist(requestDto, user);

        //then
        assertThat(result.productId()).isEqualTo(1L);
        // 한개가 이미 저장되어있는 상태에서 한개를 다시 저장했으니 수량은 두개가 된다.
        assertThat(result.quantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 조회시 반환 리스트의 개수는 장바구니에 저장되어있는 장바구니 상품의 개수와 같다.")
    public void testGetWishlist() {
        //given
        Wishlist wishlist = Wishlist.builder().id(1L).build();
        User user = User.builder().id(1L).wishListId(wishlist.getId()).build();
        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(1L).build();
        Product product3 = Product.builder().id(1L).build();
        WishlistProduct wishlistProduct1 = WishlistProduct.builder().id(1L).wishlist(wishlist).productId(product1.getId()).build();
        WishlistProduct wishlistProduct2 = WishlistProduct.builder().id(2L).wishlist(wishlist).productId(product2.getId()).build();
        WishlistProduct wishlistProduct3 = WishlistProduct.builder().id(3L).wishlist(wishlist).productId(product3.getId()).build();

        List<WishlistProduct> wishlistProducts = List.of(wishlistProduct1, wishlistProduct2, wishlistProduct3);

        wishlist = Wishlist.builder().wishlistProducts(wishlistProducts).build();

        when(wishlistRepository.findById(user.getWishListId())).thenReturn(Optional.of(wishlist));

        //when
        List<WishlistResponseDto> result = wishlistService.getUserWishlist(user);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("장바구니 상품 수량을 변경시 결과의 상품 수량은 변경시 요청한 상품 수량과 같다.")
    public void updateWishlist() {
        //given
        Wishlist wishlist = Wishlist.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        WishlistProduct wishlistProduct = WishlistProduct.builder().id(1L).wishlist(wishlist).productId(product.getId()).quantity(1).build();

        when(wishlistProductRepository.findById(wishlistProduct.getId())).thenReturn(Optional.of(wishlistProduct));

        WishListUpdateRequestDto requestDto = WishListUpdateRequestDto.builder().wishlistProductId(1L).quantity(5).build();

        //when
        WishlistResponseDto result = wishlistService.updateWishlist(requestDto);

        //then
        assertThat(result.quantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("올바르지 않은 장바구니 상품 id로 변경시 예외가 발생한다.")
    public void updateWishlistProductNotExist() {
        //given
        when(wishlistProductRepository.findById(1L)).thenReturn(Optional.empty());
        WishListUpdateRequestDto requestDto = WishListUpdateRequestDto.builder().wishlistProductId(1L).quantity(5).build();
        //when, then
        assertThatThrownBy(() -> wishlistService.updateWishlist(requestDto)).isInstanceOf(NotFoundException.class).hasMessage("해당 장바구니 상품을 찾을 수 없습니다.");
    }
}
