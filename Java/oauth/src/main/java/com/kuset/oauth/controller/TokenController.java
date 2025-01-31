package com.kuset.oauth.controller;

import com.kuset.oauth.model.Token;
import com.kuset.oauth.service.TokenService;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@RequestParam String scopes) {
        String token = tokenService.generateToken(scopes);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@RequestParam String token) {
        Optional<Token> validToken = tokenService.validateToken(token);
        if (validToken.isPresent()) {
            return ResponseEntity.ok(Map.of("scopes", validToken.get().getScopes()));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
    }
}