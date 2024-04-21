package com.heukwu.preorder.wishlist.controller;

import com.heukwu.preorder.common.dto.ApiResponse;
import com.heukwu.preorder.common.security.UserDetailsImpl;
import com.heukwu.preorder.wishlist.controller.dto.WishListAddRequestDto;
import com.heukwu.preorder.wishlist.controller.dto.WishListUpdateRequestDto;
import com.heukwu.preorder.wishlist.controller.dto.WishlistResponseDto;
import com.heukwu.preorder.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "wishlist", description = "장바구니")
@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "장바구니 상품 추가")
    @PostMapping("/wishlist")
    public ApiResponse<WishlistResponseDto> addWishlist(@RequestBody WishListAddRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WishlistResponseDto wishlistResponseDto = wishlistService.addWishlist(requestDto, userDetails.getUser());

        return ApiResponse.success(wishlistResponseDto);
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping("/wishlist")
    public ApiResponse<List<WishlistResponseDto>> getUserWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<WishlistResponseDto> wishlistResponseDtoList = wishlistService.getUserWishlist(userDetails.getUser());

        return ApiResponse.success(wishlistResponseDtoList);
    }

    @Operation(summary = "장바구니 상품 수량 변경")
    @PutMapping("/wishlist")
    public ApiResponse<WishlistResponseDto> updateWishlist(@RequestBody WishListUpdateRequestDto requestDto) {
        WishlistResponseDto wishlistResponseDto = wishlistService.updateWishlist(requestDto);

        return ApiResponse.success(wishlistResponseDto);
    }
}
