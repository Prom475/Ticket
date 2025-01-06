package com.gestion.tickets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String statut;
    private String priorite;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;



    // Méthodes pour gérer automatiquement les dates
    @PrePersist
    public void prePersist() {
        if (this.dateCreation == null) {
            this.dateCreation = LocalDateTime.now();  // Set dateCreation only if it's null
        }
        this.dateMiseAJour = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    public Departement getDepartement() {
        return departement;
    }
}
