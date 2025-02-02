package com.kuset.oauth.repository;

import com.kuset.oauth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> { // Use String as the ID type
    Optional<Token> findFirstByScopesOrderByExpiresAtDesc(String scopes);
    Optional<Token> findByTokenAndExpiresAtAfter(String token, LocalDateTime now);
}