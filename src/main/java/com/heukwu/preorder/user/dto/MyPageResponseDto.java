package com.heukwu.preorder.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageResponseDto {
    private final String username;
    private final String name;
    private final String email;
    private final String address;
    private final String phoneNumber;

    @Builder
    public MyPageResponseDto(String username, String name, String email, String address, String phoneNumber) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
