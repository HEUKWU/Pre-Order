package com.heukwu.preorder.product.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.dto.ProductRequestDto;
import com.heukwu.preorder.product.dto.ProductResponseDto;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProductList() {
        List<Product> productList = productRepository.findAll();

        return productList.stream().map(ProductResponseDto::of).toList();
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT));

        return ProductResponseDto.of(product);
    }

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .build();

        productRepository.save(product);

        return ProductResponseDto.of(product);
    }
}
