package com.gestion.tickets.repository;

import com.gestion.tickets.model.Departement;
import com.gestion.tickets.model.Ticket;
import com.gestion.tickets.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUtilisateurId(Long utilisateurId);
}

