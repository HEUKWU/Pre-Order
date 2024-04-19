package com.heukwu.preorder.user.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.common.util.EncryptUtil;
import com.heukwu.preorder.jwt.JwtUtil;
import com.heukwu.preorder.user.dto.MyPageResponseDto;
import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptUtil encryptor;

    public void signup(UserRequestDto.Signup requestDto) {
        Optional<User> findUser = userRepository.findUserByUsername(requestDto.getUsername());
        if (findUser.isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_USERNAME);
        }

        String email = requestDto.getEmail();
        String encryptedEmail = encryptor.encrypt(email);

        if (userRepository.findUserByEmail(encryptedEmail).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        String encryptedName = encryptor.encrypt(requestDto.getName());
        String encryptedAddress = encryptor.encrypt(requestDto.getAddress());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encryptedPassword)
                .name(encryptedName)
                .email(encryptedEmail)
                .address(encryptedAddress)
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    @Transactional
    public void updateAddress(String address, User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        String encryptedAddress = encryptor.encrypt(address);
        findUser.updateAddress(encryptedAddress);
    }

    @Transactional
    public void updatePhoneNumber(String phoneNumber, User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        findUser.updatePhoneNumber(phoneNumber);
    }

    @Transactional
    public void updatePassword(UserRequestDto.Password password, User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        String encodedPassword = passwordEncoder.encode(password.getPassword());
        findUser.updatePassword(encodedPassword);
    }

    public MyPageResponseDto getMyPage(User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        String name = encryptor.decrypt(findUser.getName());
        String email = encryptor.decrypt(findUser.getEmail());
        String address = encryptor.decrypt(findUser.getAddress());

        MyPageResponseDto responseDto = MyPageResponseDto.builder()
                .name(name)
                .username(findUser.getUsername())
                .email(email)
                .address(address)
                .phoneNumber(findUser.getPhoneNumber())
                .build();

        return responseDto;
    }
}
