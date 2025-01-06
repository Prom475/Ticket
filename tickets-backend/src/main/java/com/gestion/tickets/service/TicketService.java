package com.gestion.tickets.service;

import com.gestion.tickets.model.Departement;
import com.gestion.tickets.model.Ticket;
import com.gestion.tickets.model.Utilisateur;
import com.gestion.tickets.repository.DepartementRepository;
import com.gestion.tickets.repository.TicketRepository;
import com.gestion.tickets.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private TicketService ticketService; // Supprimer cet import car inutile

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Ticket createTicket(Ticket ticket) {
        // Validation de base
        if (ticket.getTitre() == null || ticket.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }

        if (ticket.getDescription() == null || ticket.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description est obligatoire");
        }

        if (ticket.getUtilisateur() == null) {
            throw new IllegalArgumentException("Un utilisateur doit être associé au ticket");
        }

        if (ticket.getDepartement() == null) {
            throw new IllegalArgumentException("Un département doit être associé au ticket");
        }

        // Définir des valeurs par défaut si nécessaire
        if (ticket.getStatut() == null || ticket.getStatut().isEmpty()) {
            ticket.setStatut("NOUVEAU");
        }

        if (ticket.getPriorite() == null || ticket.getPriorite().isEmpty()) {
            ticket.setPriorite("NORMALE");
        }

        // Associer le département
        Departement departement = departementRepository.findById(ticket.getDepartement().getId())
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        ticket.setDepartement(departement);

        // Définir les dates
        ticket.setDateCreation(LocalDateTime.now());
        ticket.setDateMiseAJour(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }


    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trouvé"));

        // Mise à jour sélective des champs
        if (ticketDetails.getTitre() != null) {
            ticket.setTitre(ticketDetails.getTitre());
        }
        if (ticketDetails.getDescription() != null) {
            ticket.setDescription(ticketDetails.getDescription());
        }
        if (ticketDetails.getStatut() != null) {
            ticket.setStatut(ticketDetails.getStatut());
        }
        if (ticketDetails.getPriorite() != null) {
            ticket.setPriorite(ticketDetails.getPriorite());
        }

        // La date de mise à jour est automatiquement gérée par @PreUpdate

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByUtilisateurId(Long utilisateurId) {
        return ticketRepository.findByUtilisateurId(utilisateurId);
    }

    public ResponseEntity<Ticket> getTicketById(Long id) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        return ticketOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Retourne 404 si le ticket n'est pas trouvé
    }

    // Méthode pour obtenir les tickets avec les détails des utilisateurs
    public List<Ticket> getAllTicketsWithUserDetails() {
        List<Ticket> tickets = ticketRepository.findAll(); // Récupération des tickets de la base
        return tickets.stream().map(ticket -> {
            // Charger les détails utilisateur à partir du ticket
            Utilisateur utilisateur = utilisateurService.findById(ticket.getUtilisateur().getId());
            ticket.setUtilisateur(utilisateur); // Ajout des détails utilisateur
            return ticket;
        }).collect(Collectors.toList());
    }

//    public Optional<String> getDepartementNameByTicketId(Long ticketId) {
//        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
//        if (ticketOptional.isPresent()) {
//            Departement departement = ticketOptional.get().getDepartement();
//            if (departement != null) {
//                return Optional.of(departement.getNom()); // Retourne le nom du département
//            }
//        }
//        return Optional.empty(); // Retourne Optional vide si le département n'est pas trouvé
//    }

    public Optional<String> getDepartementNameByTicketId(Long ticketId) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isPresent()) {
            Departement departement = ticketOptional.get().getDepartement();
            if (departement != null) {
                return Optional.of(departement.getNom()); // Retourne le nom du département
            }
        }
        return Optional.empty(); // Retourne Optional vide si le département n'est pas trouvé
    }

}
