package com.heukwu.preorder.user.dto;

import com.heukwu.preorder.common.SuccessMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class UserResponseDto {

    private int code;
    private String message;

    public static UserResponseDto of(HttpStatus status, SuccessMessage message) {
        return UserResponseDto.builder()
                .code(status.value())
                .message(message.getMessage())
                .build();
    }
}
