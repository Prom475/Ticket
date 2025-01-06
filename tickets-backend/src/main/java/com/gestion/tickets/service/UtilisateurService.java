package com.gestion.tickets.service;

import com.gestion.tickets.model.Utilisateur;
import com.gestion.tickets.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Créer un utilisateur
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        // Validation des données
        if (utilisateur.getNom() == null || utilisateur.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne doit pas être vide.");
        }
        if (utilisateur.getEmail() == null || utilisateur.getEmail().isEmpty()) {
            throw new IllegalArgumentException("L'email ne doit pas être vide.");
        }
        if (utilisateur.getRole() == null || utilisateur.getRole().isEmpty()) {
            throw new IllegalArgumentException("Le rôle ne doit pas être vide.");
        }
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit avoir au moins 8 caractères.");
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }

    // Récupérer un utilisateur par email
    public Utilisateur getUtilisateurByEmail(String email) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);
        return utilisateurOptional.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Récupération par ID
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public boolean authenticate(String email, String motDePasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            // Comparer le mot de passe encodé avec celui fourni
            return passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse());
        }
        return false; // Retourne false si l'utilisateur n'existe pas ou les informations ne correspondent pas
    }

    // Exemples d'utilisation
    public ResponseEntity<Utilisateur> getUtilisateur(Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public String getRoleById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        return utilisateur != null ? utilisateur.getRole() : null;
    }

}
