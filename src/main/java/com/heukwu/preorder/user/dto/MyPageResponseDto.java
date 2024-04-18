package com.heukwu.preorder.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDto {

    private String username;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
}
