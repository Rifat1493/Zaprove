

package com.jamiur.disbursement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private Claims parseToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        Claims claims = parseToken(token);
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            return Collections.emptyList();
        }

        // IMPORTANT: Spring Security matches the exact string in hasAuthority()
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role)) // Keep exact match (CO, RO, etc.)
                .collect(Collectors.toList());
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token); // Parse only once
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
