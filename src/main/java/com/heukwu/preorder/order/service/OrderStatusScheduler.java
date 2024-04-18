package com.heukwu.preorder.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderService orderService;

    // 매일 자정에 주문 상태 업데이트
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOrderStatus() {
        orderService.updateOrderStatus();
    }
}
