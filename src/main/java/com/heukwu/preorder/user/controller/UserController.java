package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.common.SuccessMessage;
import com.heukwu.preorder.security.UserDetailsImpl;
import com.heukwu.preorder.user.dto.MyPageResponseDto;
import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.dto.UserResponseDto;
import com.heukwu.preorder.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public UserResponseDto signup(@RequestBody @Validated UserRequestDto.Signup requestDto) {
        userService.signup(requestDto);

        return UserResponseDto.of(HttpStatus.OK, SuccessMessage.SIGNUP);
    }

    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @PutMapping("/user/address")
    public UserResponseDto updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());

        return UserResponseDto.of(HttpStatus.OK, SuccessMessage.UPDATE_ADDRESS);
    }

    @PutMapping("/user/phone")
    public UserResponseDto updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());

        return UserResponseDto.of(HttpStatus.OK, SuccessMessage.UPDATE_PHONE_NUMBER);
    }

    @PutMapping("/user/password")
    public UserResponseDto updatePassword(@RequestBody @Validated UserRequestDto.Password password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());

        return UserResponseDto.of(HttpStatus.OK, SuccessMessage.UPDATE_PASSWORD);
    }

    @GetMapping("/user")
    public MyPageResponseDto getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MyPageResponseDto MyPageResponseDto = userService.getMyPage(userDetails.getUser());

        return MyPageResponseDto;
    }
}
