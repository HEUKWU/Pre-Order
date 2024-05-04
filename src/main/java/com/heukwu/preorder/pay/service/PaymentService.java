package com.heukwu.preorder.pay.service;

import com.heukwu.preorder.pay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void payment(Long orderId) {
        log.info("payment orderId = {}", orderId);
    }
}
