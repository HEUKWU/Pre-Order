package com.heukwu.preorder.product.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.controller.dto.ProductRequestDto;
import com.heukwu.preorder.product.controller.dto.ProductResponseDto;
import com.heukwu.preorder.product.controller.dto.ProductSearch;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProductList(ProductSearch search, int size, Long cursorId) {
        Slice<Product> products = productRepository.findBySearchOption(cursorId, search, PageRequest.ofSize(size));

        return products.stream()
                .map(ProductResponseDto::toListResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT));


        return ProductResponseDto.of(product);
    }

    public void createProduct(ProductRequestDto requestDto) {

        Product product = Product.builder()
                .name(requestDto.name())
                .description(requestDto.description())
                .price(requestDto.price())
                .build();

        productRepository.save(product);
    }
}
