package com.heukwu.preorder.product.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {
    private String name;
    private String description;
    private int price;
    private int quantity;
}
