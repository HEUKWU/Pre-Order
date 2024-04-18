package com.heukwu.preorder.user.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.jwt.JwtUtil;
import com.heukwu.preorder.user.dto.MyPageResponseDto;
import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${encrypt.password}")
    String encryptPassword;

    @Value("${encrypt.salt}")
    String encryptSalt;

    public void signup(UserRequestDto.Signup requestDto) {
        Optional<User> findUser = userRepository.findUserByUsername(requestDto.getUsername());
        if (findUser.isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_USERNAME);
        }

        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        String encryptedName = encryptValue(requestDto.getName());
        String encryptedAddress = encryptValue(requestDto.getAddress());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encryptedPassword)
                .name(encryptedName)
                .email(requestDto.getEmail())
                .address(encryptedAddress)
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }

    private String encryptValue(String value) {
        return Encryptors.text(encryptPassword, encryptSalt).encrypt(value);
    }

    private String decryptValue(String value) {
        return Encryptors.text(encryptPassword, encryptSalt).decrypt(value);
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

        String encryptedAddress = encryptValue(address);
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

        String name = decryptValue(findUser.getName());
        String address = decryptValue(findUser.getAddress());

        MyPageResponseDto responseDto = MyPageResponseDto.builder()
                .name(name)
                .username(findUser.getUsername())
                .email(findUser.getEmail())
                .address(address)
                .phoneNumber(findUser.getPhoneNumber())
                .build();

        return responseDto;
    }
}
