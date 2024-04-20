package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.product.controller.dto.ProductRequestDto;
import com.heukwu.preorder.product.controller.dto.ProductResponseDto;
import com.heukwu.preorder.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ApiResponse<List<ProductResponseDto>> getProductList() {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList();

        return ApiResponse.success(productResponseDtoList);
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProduct(productId);

        return ApiResponse.success(productResponseDto);
    }

    @PostMapping("/product")
    public ApiResponse<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(requestDto);

        return ApiResponse.success(productResponseDto);
    }
}
