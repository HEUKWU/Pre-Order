package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.user.controller.dto.LoginRequestDto;
import com.heukwu.preorder.user.controller.dto.PasswordChangeRequestDto;
import com.heukwu.preorder.user.controller.dto.MyPageResponseDto;
import com.heukwu.preorder.user.controller.dto.SignupRequestDto;
import com.heukwu.preorder.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "회원 관리")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/user/signup")
    public ApiResponse<Boolean> signup(@RequestBody @Validated SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ApiResponse.success();
    }

    @Operation(summary = "로그인")
    @PostMapping("/user/login")
    public ApiResponse<Boolean> login(@RequestBody LoginRequestDto requestDto) {
        return ApiResponse.success();
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @Operation(summary = "주소 변경")
    @PutMapping("/user/address")
    public ApiResponse<Boolean> updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());

        return ApiResponse.success();
    }

    @Operation(summary = "휴대폰 번호 변경")
    @PutMapping("/user/phone")
    public ApiResponse<Boolean> updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());

        return ApiResponse.success();
    }

    @Operation(summary = "비밀번호 변경")
    @PutMapping("/user/password")
    public ApiResponse<Boolean> updatePassword(@RequestBody @Validated PasswordChangeRequestDto password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());

        return ApiResponse.success();
    }

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/user")
    public ApiResponse<MyPageResponseDto> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MyPageResponseDto MyPageResponseDto = userService.getMyPage(userDetails.getUser());

        return ApiResponse.success(MyPageResponseDto);
    }
}
