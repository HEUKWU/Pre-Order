package com.heukwu.preorder.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity(name = "orders")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int quantity;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProduct;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isNotCancelable() {
        return this.status != OrderStatus.CREATED;
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELED;
    }

    public boolean isNotReturnable() {
        boolean isReturnableStatus = this.status != OrderStatus.COMPLETE;
        boolean isReturnableDate = LocalDate.now().isAfter(this.modifiedAt.plusDays(1));
        return isReturnableDate && isReturnableStatus;
    }

    public void returnOrder() {
        this.status = OrderStatus.RETURNING;
    }
}
