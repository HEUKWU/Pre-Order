package com.heukwu.preorder.user.controller.dto;

import lombok.Builder;

@Builder
public record MyPageResponseDto(

        String username,
        String name,
        String email,
        String address,
        String phoneNumber
) { }
