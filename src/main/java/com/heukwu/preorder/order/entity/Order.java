package com.heukwu.preorder.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "orders")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int totalPrice;

    private Long userId;

    @OneToMany
    private List<OrderProduct> orderProductList;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isNotCancelableStatus() {
        return this.status != OrderStatus.CREATED;
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELED;
    }

    public boolean isNotReturnableStatus() {
        return this.status != OrderStatus.COMPLETED_SHIPMENT;
    }

    public boolean isNotReturnableDate() {
        return LocalDate.now().isAfter(this.modifiedAt.plusDays(1));
    }

    public void returnOrder() {
        this.status = OrderStatus.RETURNING;
    }
}
