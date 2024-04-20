package com.heukwu.preorder.product.service;

import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.controller.dto.ProductResponseDto;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품리스트 전체 조회시 반환된 리스트의 개수는 저장되어있는 상품의 개수와 같다.")
    public void testGetProductList() {
        //given
        Product product1 = Product.builder().id(1L).name("P1").description("D1").price(1000).quantity(10).build();
        Product product2 = Product.builder().id(1L).name("P1").description("D1").price(1000).quantity(10).build();
        Product product3 = Product.builder().id(1L).name("P1").description("D1").price(1000).quantity(10).build();

        List<Product> productList = List.of(product1, product2, product3);

        when(productRepository.findAll()).thenReturn(productList);

        //when
        List<ProductResponseDto> result = productService.getProductList();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 상세조회시 반환 결과의 상품 이름과 상세설명은 조회한 상품의 이름과 상세설명과 같다.")
    public void testGetProductDetails() {
        //given
        Product product = Product.builder().id(1L).name("Product").description("Description").price(1000).quantity(10).build();

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        //when
        ProductResponseDto result = productService.getProduct(1L);

        //then
        assertThat(result.name()).isEqualTo("Product");
        assertThat(result.description()).isEqualTo("Description");
    }

    @Test
    @DisplayName("올바르지 않은 상품 id로 조회시 예외가 발생한다.")
    public void getProductNotExist() {
        //given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> productService.getProduct(1L)).isInstanceOf(NotFoundException.class).hasMessage("해당 상품을 찾을 수 없습니다.");
    }
}
