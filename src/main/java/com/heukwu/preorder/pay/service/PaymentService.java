package com.heukwu.preorder.pay.service;

import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.order.entity.Order;
import com.heukwu.preorder.order.entity.OrderStatus;
import com.heukwu.preorder.order.repository.OrderRepository;
import com.heukwu.preorder.pay.entity.Payment;
import com.heukwu.preorder.pay.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    @Transactional
    public void payment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_ORDER)
        );

        log.info("payment orderId = {}", order.getId());

        Random random = new Random();
        int randomNumber = random.nextInt(100);

        if (randomNumber < 20) {
            log.info("결제 실패");
            return;
        }

        Payment payment = new Payment(order.getId());
        paymentRepository.save(payment);

        order.updateStatus(OrderStatus.COMPLETED_PAYMENT);
        log.info("결제 성공");
    }
}
