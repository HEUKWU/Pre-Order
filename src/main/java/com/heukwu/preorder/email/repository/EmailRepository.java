package com.heukwu.preorder.email.repository;

import com.heukwu.preorder.email.entity.EmailVerificationHistory;
import com.heukwu.preorder.email.entity.EmailVerificationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerificationHistory, Long> {

    Optional<EmailVerificationHistory> findEmailByEmail(String email);

    Optional<EmailVerificationHistory> findByEmailAndVerificationStatus(String email, EmailVerificationStatusEnum statusEnum);

}
