package com.heukwu.preorder.user.controller;

import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public void signup(@RequestBody @Validated UserRequestDto.Signup requestDto) {
        userService.signup(requestDto);
    }
}
