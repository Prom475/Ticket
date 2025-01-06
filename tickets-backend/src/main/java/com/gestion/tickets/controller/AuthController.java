package com.gestion.tickets.controller;

import com.gestion.tickets.dto.LoginRequest;
import com.gestion.tickets.model.Utilisateur;
import com.gestion.tickets.service.UtilisateurService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilisateurService utilisateurService;

    @Value("${jwt.secret-key}")
    private String secretKey; // Clé secrète lue depuis les propriétés de configuration

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = utilisateurService.authenticate(loginRequest.getEmail(), loginRequest.getMotDePasse());

        if (isAuthenticated) {
            String token = generateJwtToken(loginRequest.getEmail()); // Génération du token JWT
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur newUser = utilisateurService.createUtilisateur(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    private String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 jour d'expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @GetMapping("/user/{id}/role")
    public ResponseEntity<?> getUserRoleById(@PathVariable Long id) {
        try {
            String role = utilisateurService.getRoleById(id); // Appel au service pour obtenir le rôle
            if (role != null) {
                return ResponseEntity.ok(Map.of("role", role));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du rôle : " + e.getMessage());
        }
    }
}
