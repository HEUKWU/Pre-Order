package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.product.dto.ProductRequestDto;
import com.heukwu.preorder.product.dto.ProductResponseDto;
import com.heukwu.preorder.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseDto<List<ProductResponseDto>> getProductList() {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList();

        return ResponseDto.success(productResponseDtoList);
    }

    @GetMapping("/product/{productId}")
    public ResponseDto<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProduct(productId);

        return ResponseDto.success(productResponseDto);
    }

    @PostMapping("/product")
    public ResponseDto<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(requestDto);

        return ResponseDto.success(productResponseDto);
    }
}
