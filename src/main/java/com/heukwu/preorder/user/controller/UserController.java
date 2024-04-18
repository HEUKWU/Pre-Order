package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.common.SuccessMessage;
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
    public ResponseDto<String> signup(@RequestBody @Validated UserRequestDto.Signup requestDto) {
        userService.signup(requestDto);

        return ResponseDto.of(SuccessMessage.SIGNUP.getMessage());
    }

    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @PutMapping("/user/address")
    public ResponseDto<String> updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());

        return ResponseDto.of(SuccessMessage.UPDATE_ADDRESS.getMessage());
    }

    @PutMapping("/user/phone")
    public ResponseDto<String> updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());

        return ResponseDto.of(SuccessMessage.UPDATE_PHONE_NUMBER.getMessage());
    }

    @PutMapping("/user/password")
    public ResponseDto<String> updatePassword(@RequestBody @Validated UserRequestDto.Password password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());

        return ResponseDto.of(SuccessMessage.UPDATE_PASSWORD.getMessage());
    }

    @GetMapping("/user")
    public ResponseDto<MyPageResponseDto> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        MyPageResponseDto MyPageResponseDto = userService.getMyPage(userDetails.getUser());

        return ResponseDto.of(MyPageResponseDto);
    }
}
