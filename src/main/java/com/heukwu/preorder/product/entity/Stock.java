package com.heukwu.preorder.product.entity;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private int quantity;

    public Stock(int quantity) {
        this.quantity = quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;

        if (this.quantity < 0) {
            throw new BusinessException(ErrorMessage.OUT_OF_STOCK_EXCEPTION);
        }
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}


