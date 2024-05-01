package com.heukwu.preorder.product.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.controller.dto.ProductStockResponseDto;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    public ProductStockResponseDto getProductStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        return ProductStockResponseDto.of(product.getId(), product.getStock().getQuantity());
    }
}
