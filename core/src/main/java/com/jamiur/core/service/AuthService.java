package com.jamiur.core.service;

import com.jamiur.core.model.dto.LoginRequest;
import com.jamiur.core.model.dto.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Set<String> blacklistedTokens = new HashSet<>();

    public LoginResponse login(LoginRequest request) {
        // Replace with real user validation
        if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = generateToken(request.getUsername());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            blacklistedTokens.add(token);
        }
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token) {
        return !blacklistedTokens.contains(token);
    }
}