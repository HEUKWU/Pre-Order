package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.product.controller.dto.ProductRequestDto;
import com.heukwu.preorder.product.controller.dto.ProductResponseDto;
import com.heukwu.preorder.product.controller.dto.ProductSearch;
import com.heukwu.preorder.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "product", description = "상품")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록")
    @PostMapping("/product")
    public ApiResponse<Boolean> createProduct(@RequestBody ProductRequestDto requestDto) {
        productService.createProduct(requestDto);

        return ApiResponse.success();
    }

    @Operation(summary = "상품 목록 조회")
    @GetMapping("/product")
    public ApiResponse<List<ProductResponseDto>> getProductList(ProductSearch search, @RequestParam int size, Long cursorId) {
        List<ProductResponseDto> productResponseDtoList = productService.getProductList(search, size, cursorId);

        return ApiResponse.success(productResponseDtoList);
    }

    @Operation(summary = "상품 상세 조회")
    @GetMapping("/product/{productId}")
    public ApiResponse<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProduct(productId);

        return ApiResponse.success(productResponseDto);
    }
}
