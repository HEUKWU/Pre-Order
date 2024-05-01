package com.heukwu.preorder.product.repository;

import com.heukwu.preorder.product.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
