package com.heukwu.preorder.user.service;

import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserRequestDto.Signup requestDto) {
        if (userRepository.findUserEntityByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String username = passwordEncoder.encode(requestDto.getUsername());
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = passwordEncoder.encode(requestDto.getEmail());
        String address = passwordEncoder.encode(requestDto.getAddress());

        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .address(address)
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }
}
