package com.heukwu.preorder.product.entity;

import com.heukwu.preorder.common.exception.BusinessException;
import com.heukwu.preorder.common.exception.ErrorMessage;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Stock")
@Getter
@NoArgsConstructor
public class Stock {

    @Id
    private String id;

    private int quantity;

    public Stock(long productId, int quantity) {
        this.id = String.valueOf(productId);
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
