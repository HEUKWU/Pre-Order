package com.heukwu.preorder.order.controller;

import com.heukwu.preorder.common.SuccessMessage;
import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.order.dto.OrderRequestDto;
import com.heukwu.preorder.order.dto.OrderResponseDto;
import com.heukwu.preorder.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseDto<OrderResponseDto> orderProduct(@RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto orderResponseDto = orderService.orderProduct(requestDto, userDetails.getUser());

        return ResponseDto.of(orderResponseDto);
    }

    @GetMapping("/order")
    public ResponseDto<List<OrderResponseDto>> getUserOrderInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponseDto> userOrderInfo = orderService.getUserOrderInfo(userDetails.getUser());

        return ResponseDto.of(userOrderInfo);
    }

    @PostMapping("/order-wishlist")
    public ResponseDto<List<OrderResponseDto>> orderWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponseDto> orderResponseDtoList = orderService.orderWishlist(userDetails.getUser());

        return ResponseDto.of(orderResponseDtoList);
    }

    @DeleteMapping("/order")
    public ResponseDto<String> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(userDetails.getUser());

        return ResponseDto.of(SuccessMessage.CANCEL_ORDER.getMessage());
    }

    @PutMapping("/order")
    public ResponseDto<String> returnOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.returnOrder(userDetails.getUser());

        return ResponseDto.of(SuccessMessage.RETURN_ORDER.getMessage());
    }
}
