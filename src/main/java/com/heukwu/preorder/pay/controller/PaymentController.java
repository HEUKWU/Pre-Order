package com.heukwu.preorder.pay.controller;

import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public void payment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        
    }
}
