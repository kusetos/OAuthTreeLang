package com.kuset.oauth.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    private String token;
    private String scopes;
    private Instant expiresAt;

    public Token() {}

    public Token(String token, String scopes, Instant expiresAt) {
        this.token = token;
        this.scopes = scopes;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getScopes() { return scopes; }
    public void setScopes(String scopes) { this.scopes = scopes; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
