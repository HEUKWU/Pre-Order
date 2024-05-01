package com.heukwu.preorder.product.controller;

import com.heukwu.preorder.product.controller.dto.ProductStockResponseDto;
import com.heukwu.preorder.product.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stock/{productId}")
    public ProductStockResponseDto getProductStock(@PathVariable Long productId) {
        return stockService.getProductStock(productId);
    }
}
