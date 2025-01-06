package com.gestion.tickets.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "my-secure-secret-key"; // Use the same secure key

    public static String generateToken(String email, Long userId, long expirationTimeInMillis) {
        return Jwts.builder()
                .setSubject(email)   // Email or user ID
                .claim("id", userId) // User ID
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
