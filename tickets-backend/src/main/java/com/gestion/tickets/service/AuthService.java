package com.gestion.tickets.service;

import com.gestion.tickets.configuration.JwtTokenUtil;

public class AuthService {

    private static final long TOKEN_EXPIRATION_TIME = 86400000; // 24 heures en millisecondes

    public String generateJwtToken(String email, Long userId) {
        return JwtTokenUtil.generateToken(email, userId, TOKEN_EXPIRATION_TIME);
    }
}

