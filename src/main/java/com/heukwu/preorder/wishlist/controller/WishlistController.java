package com.heukwu.preorder.wishlist.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.wishlist.dto.WishlistRequestDto;
import com.heukwu.preorder.wishlist.dto.WishlistResponseDto;
import com.heukwu.preorder.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/wishlist")
    public ResponseDto<WishlistResponseDto> addWishlist(@RequestBody WishlistRequestDto.Create requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WishlistResponseDto wishlistResponseDto = wishlistService.addWishlist(requestDto, userDetails.getUser());

        return ResponseDto.success(wishlistResponseDto);
    }

    @GetMapping("/wishlist")
    public ResponseDto<List<WishlistResponseDto>> getUserWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<WishlistResponseDto> wishlistResponseDtoList = wishlistService.getUserWishlist(userDetails.getUser());

        return ResponseDto.success(wishlistResponseDtoList);
    }

    @PutMapping("/wishlist")
    public ResponseDto<WishlistResponseDto> updateWishlist(@RequestBody WishlistRequestDto.Update requestDto) {
        WishlistResponseDto wishlistResponseDto = wishlistService.updateWishlist(requestDto);

        return ResponseDto.success(wishlistResponseDto);
    }
}
