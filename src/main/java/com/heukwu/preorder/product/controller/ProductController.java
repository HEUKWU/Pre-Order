package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.product.dto.ProductResponseDto;
import com.heukwu.preorder.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseDto<List<ProductResponseDto>> getProductList() {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList();

        return ResponseDto.of(productResponseDtoList);
    }

    @GetMapping("/product/{productId}")
    public ResponseDto<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProduct(productId);

        return ResponseDto.of(productResponseDto);
    }
}
