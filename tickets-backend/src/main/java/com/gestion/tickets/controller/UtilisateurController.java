package com.gestion.tickets.controller;

import com.gestion.tickets.model.Utilisateur;
import com.gestion.tickets.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.createUtilisateur(utilisateur);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.getUtilisateurById(id);
        return utilisateurOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public Utilisateur getUtilisateurByEmail(@PathVariable String email) {
        return utilisateurService.getUtilisateurByEmail(email);
    }

    @GetMapping("/idByEmail/{email}")
    public ResponseEntity<Long> getUtilisateurIdByEmail(@PathVariable String email) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur.getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }
}
