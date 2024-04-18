package com.heukwu.preorder.wishlist.controller;

import com.heukwu.preorder.common.dto.ResponseDto;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.wishlist.dto.WishlistResponseDto;
import com.heukwu.preorder.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/wishlist/{productId}")
    public ResponseDto<WishlistResponseDto> addWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WishlistResponseDto wishlistResponseDto = wishlistService.addWishlist(productId, userDetails.getUser());

        return ResponseDto.of(wishlistResponseDto);
    }
}
