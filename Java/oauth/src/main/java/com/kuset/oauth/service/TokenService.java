package com.kuset.oauth.service;

import com.kuset.oauth.model.Token;
import com.kuset.oauth.repository.TokenRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

import java.time.Instant;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(String scopes) {
        Instant now = Instant.now();
        Instant expiryTime = now.plusSeconds(7200); // Token expires in 2 hours

        // Generate a random token
        String tokenValue = UUID.randomUUID().toString();

        Token token = new Token(tokenValue, scopes, expiryTime);
        tokenRepository.save(token);
        return tokenValue;
    }

    public Optional<Token> validateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findById(token);

        if (tokenOptional.isPresent()) {
            Token storedToken = tokenOptional.get();
            
            // Check if token is expired
            if (storedToken.getExpiresAt().isAfter(Instant.now())) {
                return Optional.of(storedToken); // Valid token
            } else {
                tokenRepository.delete(storedToken); // Delete expired token
            }
        }
        return Optional.empty(); // Token is invalid or expired
    }
}