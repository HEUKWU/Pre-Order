package com.heukwu.preorder.pay.controller;

import com.heukwu.preorder.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment/{orderId}")
    public void payment(@PathVariable Long orderId) {
        paymentService.payment(orderId);
    }
}
