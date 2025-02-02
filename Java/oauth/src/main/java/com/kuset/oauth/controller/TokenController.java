package com.kuset.oauth.controller;

import com.kuset.oauth.service.TokenService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<TokenService.TokenResponse> issueToken(
        @RequestParam String scopes
    ) {
        return ResponseEntity.ok(tokenService.issueToken(scopes));
    }

    @GetMapping("/check")
    public ResponseEntity<TokenService.ScopesResponse> checkToken(
        @RequestParam String token
    ) {
        return ResponseEntity.ok(tokenService.checkToken(token));
    }

    @ExceptionHandler(TokenService.TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenService.TokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}