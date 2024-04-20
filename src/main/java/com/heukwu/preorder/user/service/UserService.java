package com.heukwu.preorder.user.service;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import com.heukwu.preorder.common.exception.NotFoundException;
import com.heukwu.preorder.common.util.EncryptUtil;
import com.heukwu.preorder.email.entity.EmailVerificationStatusEnum;
import com.heukwu.preorder.email.repository.EmailRepository;
import com.heukwu.preorder.jwt.JwtUtil;
import com.heukwu.preorder.user.dto.MyPageResponseDto;
import com.heukwu.preorder.user.dto.UserRequestDto;
import com.heukwu.preorder.user.entity.User;
import com.heukwu.preorder.user.entity.UserRoleEnum;
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
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptUtil encryptor;

    public void signup(UserRequestDto.Signup requestDto) {
        Optional<User> findUser = userRepository.findUserByUsername(requestDto.getUsername());
        if (findUser.isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_USERNAME);
        }

        // 인증된 이메일인지 검증
        if (emailRepository.findByEmailAndVerificationStatus(encryptor.encrypt(requestDto.getEmail()), EmailVerificationStatusEnum.CREATED).isEmpty()) {
            throw new BusinessException(ErrorMessage.UNAUTHENTICATED_EMAIL);
        }

        String encryptedEmail = encryptor.encrypt(requestDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        String encryptedName = encryptor.encrypt(requestDto.getName());
        String encryptedAddress = encryptor.encrypt(requestDto.getAddress());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encryptedPassword)
                .role(UserRoleEnum.USER)
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
    public void updatePassword(UserRequestDto.Password requestDto, User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        verificationPassword(requestDto, user);

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        findUser.updatePassword(encodedPassword);
    }

    // 기존 비밀번호 검증
    private void verificationPassword(UserRequestDto.Password requestDto, User user) {
        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorMessage.WRONG_PASSWORD);
        }

        if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new BusinessException(ErrorMessage.SAME_PASSWORD);
        }
    }

    public MyPageResponseDto getMyPage(User user) {
        User findUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND_USER)
        );

        String name = encryptor.decrypt(findUser.getName());
        String email = encryptor.decrypt(findUser.getEmail());
        String address = encryptor.decrypt(findUser.getAddress());

        return MyPageResponseDto.builder()
                .name(name)
                .username(findUser.getUsername())
                .email(email)
                .address(address)
                .phoneNumber(findUser.getPhoneNumber())
                .build();
    }
}
