package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.security.UserDetailsImpl;
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
    public void signup(@RequestBody @Validated UserRequestDto.Signup requestDto) {
        userService.signup(requestDto);
    }

    @GetMapping("/user/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @PutMapping("/user/address")
    public void updateAddress(@RequestParam @NotBlank String address, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateAddress(address, userDetails.getUser());
    }

    @PutMapping("/user/phone")
    public void updatePhoneNumber(@RequestParam @NotBlank String phoneNumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePhoneNumber(phoneNumber, userDetails.getUser());
    }

    @PutMapping("/user/password")
    public void updatePassword(@RequestBody @Validated UserRequestDto.Password password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(password, userDetails.getUser());
    }
}
