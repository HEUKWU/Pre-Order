package com.heukwu.preorder.product.repository;

import com.heukwu.preorder.product.entity.Stock;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash("Stock")
public interface StockRepository extends CrudRepository<Stock, String> {
}
