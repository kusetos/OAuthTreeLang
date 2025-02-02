package com.kuset.oauth.service;

import com.kuset.oauth.model.Token;
import com.kuset.oauth.repository.TokenRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Service
public class TokenService {
    
    @Autowired
    private TokenRepository tokenRepository;

    public TokenResponse issueToken(String scopes) {
        LocalDateTime now = LocalDateTime.now();
        
        Optional<Token> existingToken = tokenRepository.findFirstByScopesOrderByExpiresAtDesc(scopes);
        if (existingToken.isPresent()) {
            long remaining = ChronoUnit.SECONDS.between(now, existingToken.get().getExpiresAt());
            if (remaining > 1800) {
                return new TokenResponse(
                    existingToken.get().getToken(),
                    remaining
                );
            }
        }
        
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = now.plusHours(2);
        
        Token token = new Token();
        token.setToken(newToken);
        token.setScopes(scopes);
        token.setExpiresAt(expiresAt);
        tokenRepository.save(token);
        
        return new TokenResponse(newToken, 7200);
    }

    public ScopesResponse checkToken(String token) {
        Optional<Token> validToken = tokenRepository.findByTokenAndExpiresAtAfter(
            token, 
            LocalDateTime.now()
        );
        
        if (validToken.isEmpty()) {
            throw new TokenException("Invalid or expired token");
        }
        
        return new ScopesResponse(validToken.get().getScopes());
    }
    
    // DTOs
    public record TokenResponse(String token, long expiresIn) {}
    public record ScopesResponse(String scopes) {}
    
    // Custom Exception
    public static class TokenException extends RuntimeException {
        public TokenException(String message) {
            super(message);
        }
    }
}