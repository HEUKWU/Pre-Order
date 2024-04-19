package com.heukwu.preorder.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequestDto {
    private String name;
    private String description;
    private int price;
    private int quantity;
}
