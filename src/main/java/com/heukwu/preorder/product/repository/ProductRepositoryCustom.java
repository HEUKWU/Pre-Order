package com.heukwu.preorder.product.repository;

import com.heukwu.preorder.product.controller.dto.ProductSearch;
import com.heukwu.preorder.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<Product> findBySearchOption(Long cursorId, ProductSearch search, Pageable pageable);
}
