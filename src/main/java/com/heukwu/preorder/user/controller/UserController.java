package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.user.controller.dto.PasswordChangeRequestDto;
import com.heukwu.preorder.user.controller.dto.MyPageResponseDto;
import com.heukwu.preorder.user.controller.dto.SignupRequestDto;
import com.heukwu.preorder.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ApiResponse<Boolean> signup(@RequestBody @Validated SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ApiResponse.success();
    }

    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @PutMapping("/user/address")
    public ApiResponse<Boolean> updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());

        return ApiResponse.success();
    }

    @PutMapping("/user/phone")
    public ApiResponse<Boolean> updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());

        return ApiResponse.success();
    }

    @PutMapping("/user/password")
    public ApiResponse<Boolean> updatePassword(@RequestBody @Validated PasswordChangeRequestDto password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());

        return ApiResponse.success();
    }

    @GetMapping("/user")
    public ApiResponse<MyPageResponseDto> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MyPageResponseDto MyPageResponseDto = userService.getMyPage(userDetails.getUser());

        return ApiResponse.success(MyPageResponseDto);
    }
}
