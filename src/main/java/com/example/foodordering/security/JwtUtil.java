package com.example.foodordering.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // encryption key(later we add it to application.yml)
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // token expiration time (1 hour)
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    // generate JWT
    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // extract email from token
    public static String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // extract role
    public static String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // check if token is valid
    public static boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // read data inside the token
    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
