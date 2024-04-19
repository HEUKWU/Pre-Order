package com.heukwu.preorder.email.repository;

import com.heukwu.preorder.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findEmailByEmail(String email);
}
