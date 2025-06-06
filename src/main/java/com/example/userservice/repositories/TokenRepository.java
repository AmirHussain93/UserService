package com.example.userservice.repositories;

import com.example.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token save(Token token);
    Optional<Token> findByTokenValueAndDeletedAndExpiryAtGreaterThan(String tokenValue, boolean deleted, Date expiryAt);

    String tokenValue(String tokenValue);
}
