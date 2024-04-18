package com.heukwu.preorder.order.repository;

import com.heukwu.preorder.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
