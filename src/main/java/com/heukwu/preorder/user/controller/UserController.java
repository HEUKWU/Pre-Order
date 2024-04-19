package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.user.dto.MyPageResponseDto;
import com.heukwu.preorder.user.dto.UserRequestDto;
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
    public ResponseDto<Boolean> signup(@RequestBody @Validated UserRequestDto.Signup requestDto) {
        userService.signup(requestDto);

        return ResponseDto.success();
    }

    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @PutMapping("/user/address")
    public ResponseDto<Boolean> updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());

        return ResponseDto.success();
    }

    @PutMapping("/user/phone")
    public ResponseDto<Boolean> updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());

        return ResponseDto.success();
    }

    @PutMapping("/user/password")
    public ResponseDto<Boolean> updatePassword(@RequestBody @Validated UserRequestDto.Password password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());

        return ResponseDto.success();
    }

    @GetMapping("/user")
    public ResponseDto<MyPageResponseDto> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MyPageResponseDto MyPageResponseDto = userService.getMyPage(userDetails.getUser());

        return ResponseDto.success(MyPageResponseDto);
    }
}
