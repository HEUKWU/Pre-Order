package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.product.controller.dto.ProductStockRequestDto;
import com.heukwu.preorder.product.controller.dto.ProductStockResponseDto;
import com.heukwu.preorder.product.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/stock")
    public ApiResponse<Boolean> addProductStock(@RequestBody ProductStockRequestDto requestDto) {
        stockService.addProductStock(requestDto);

        return ApiResponse.success();
    }

    @GetMapping("/stock/{productId}")
    public ApiResponse<ProductStockResponseDto> getProductStock(@PathVariable Long productId) {
        return ApiResponse.success(stockService.getProductStock(productId));
    }
}
