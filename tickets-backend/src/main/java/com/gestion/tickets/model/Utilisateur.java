package com.gestion.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Proxy;

import java.util.List;

@Entity
@Proxy(lazy = false) // Désactive les proxies Hibernate
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne doit pas être vide.")
    private String nom;

    @Email(message = "L'email doit être valide.")
    @NotBlank(message = "L'email ne doit pas être vide.")
    private String email;

    @NotBlank(message = "Le rôle ne doit pas être vide.")
    private String role; // Exemple : "ADMIN", "TECHNICIEN", "UTILISATEUR"

    @Size(min = 8, message = "Le mot de passe doit avoir au moins 8 caractères.")
    private String motDePasse;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ticket> tickets;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
