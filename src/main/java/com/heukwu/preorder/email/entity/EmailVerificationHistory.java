package com.heukwu.preorder.email.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class EmailVerificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    @Enumerated(value = EnumType.STRING)
    private EmailVerificationStatusEnum verificationStatus;

    public void verified() {
        verificationStatus = EmailVerificationStatusEnum.VERIFIED;
    }
}
