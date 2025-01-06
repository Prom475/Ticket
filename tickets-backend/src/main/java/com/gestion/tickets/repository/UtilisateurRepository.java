package com.gestion.tickets.repository;

import com.gestion.tickets.model.Utilisateur;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    @EntityGraph(attributePaths = "tickets") // Charge les tickets en même temps
    Optional<Utilisateur> findById(Long id);
}


