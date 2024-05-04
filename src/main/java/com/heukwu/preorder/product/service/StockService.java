package com.heukwu.preorder.product.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.product.controller.dto.ProductStockRequestDto;
import com.heukwu.preorder.product.controller.dto.ProductStockResponseDto;
import com.heukwu.preorder.product.entity.Product;
import com.heukwu.preorder.product.entity.Stock;
import com.heukwu.preorder.product.repository.ProductRepository;
import com.heukwu.preorder.product.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public void addProductStock(ProductStockRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.productId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        Stock stock = new Stock(product.getId(), requestDto.quantity());

        stockRepository.save(stock);
    }

    public ProductStockResponseDto getProductStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
        );

        Stock stock = stockRepository.findById(String.valueOf(productId)).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Not_FOUND_STOCK)
        );

        return ProductStockResponseDto.of(product.getId(), stock.getQuantity());
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void syncProductQuantity() {
        Iterable<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            syncQuantity(stock);
        }
    }

    @Transactional
    public void syncQuantity(Stock stock) {
        if (stock != null) {
            long productId = Long.parseLong(stock.getId());
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.NOT_FOUND_PRODUCT)
            );

            product.syncQuantity(stock.getQuantity());
        }
    }
}
