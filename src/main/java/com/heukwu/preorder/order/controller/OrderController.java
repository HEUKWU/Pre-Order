package com.heukwu.preorder.order.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.order.controller.dto.OrderRequestDto;
import com.heukwu.preorder.order.controller.dto.OrderResponseDto;
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
    public ApiResponse<OrderResponseDto> orderProduct(@RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponseDto orderResponseDto = orderService.orderProduct(requestDto, userDetails.getUser());

        return ApiResponse.success(orderResponseDto);
    }

    @GetMapping("/order")
    public ApiResponse<List<OrderResponseDto>> getUserOrderInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponseDto> userOrderInfo = orderService.getUserOrderInfo(userDetails.getUser());

        return ApiResponse.success(userOrderInfo);
    }

    @PostMapping("/order-wishlist")
    public ApiResponse<List<OrderResponseDto>> orderWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponseDto> orderResponseDtoList = orderService.orderWishlist(userDetails.getUser());

        return ApiResponse.success(orderResponseDtoList);
    }

    @DeleteMapping("/order")
    public ApiResponse<Boolean> cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(userDetails.getUser());

        return ApiResponse.success();
    }

    @PutMapping("/order")
    public ApiResponse<Boolean> returnOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.returnOrder(userDetails.getUser());

        return ApiResponse.success();
    }
}
