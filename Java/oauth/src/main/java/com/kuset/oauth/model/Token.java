package com.kuset.oauth.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @Column(name = "token") // Map to the "token" column in the table
    private String token;

    private String scopes;
    private LocalDateTime expiresAt;

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getScopes() { return scopes; }
    public void setScopes(String scopes) { this.scopes = scopes; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}