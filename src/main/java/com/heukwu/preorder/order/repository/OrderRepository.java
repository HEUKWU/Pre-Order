package com.heukwu.preorder.order.repository;

import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId);

    List<Order> findAllByStatusAndModifiedAtBefore(OrderStatus status, LocalDateTime dateTime);
}
