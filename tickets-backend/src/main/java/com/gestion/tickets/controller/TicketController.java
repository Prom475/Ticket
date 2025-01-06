package com.gestion.tickets.controller;

import com.gestion.tickets.dto.TicketDTO;
import com.gestion.tickets.model.Departement;
import com.gestion.tickets.model.Ticket;
import com.gestion.tickets.model.Utilisateur;
import com.gestion.tickets.service.DepartementService;
import com.gestion.tickets.service.TicketService;
import com.gestion.tickets.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private DepartementService departementService;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTicketsWithUserDetails(); // Utilisation du service pour obtenir les détails utilisateur
        return ResponseEntity.ok(tickets); // Retourner les tickets avec les détails utilisateur
    }


    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        try {
            // Vérifier que l'ID utilisateur est présent
            if (ticketDTO.getUtilisateurId() == null) {
                return ResponseEntity.badRequest().body("ID Utilisateur obligatoire");
            }

            // Vérifier que l'ID du département est présent
            if (ticketDTO.getDepartementId() == null) {
                return ResponseEntity.badRequest().body("ID Département obligatoire");
            }

            // Créer un ticket à partir du DTO
            Ticket ticket = new Ticket();
            ticket.setTitre(ticketDTO.getTitre());
            ticket.setDescription(ticketDTO.getDescription());
            ticket.setStatut(ticketDTO.getStatut());
            ticket.setPriorite(ticketDTO.getPriorite());

            // Récupérer et définir l'utilisateur
            Utilisateur utilisateur = utilisateurService.findById(ticketDTO.getUtilisateurId());
            if (utilisateur == null) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé");
            }
            ticket.setUtilisateur(utilisateur);

            // Récupérer et définir le département
            Departement departement = departementService.findById(ticketDTO.getDepartementId());
            if (departement == null) {
                return ResponseEntity.badRequest().body("Département non trouvé");
            }
            ticket.setDepartement(departement);

            Ticket createdTicket = ticketService.createTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDetails) {
        // Retrieve the ticket by ID
        ResponseEntity<Ticket> responseEntity = ticketService.getTicketById(id);
        if (!responseEntity.hasBody()) {
            return ResponseEntity.notFound().build(); // Return 404 if the ticket is not found
        }

        Ticket ticket = responseEntity.getBody();  // Get the ticket from the ResponseEntity

        // Update ticket fields
        ticket.setTitre(ticketDetails.getTitre());
        ticket.setDescription(ticketDetails.getDescription());
        ticket.setStatut(ticketDetails.getStatut());
        ticket.setPriorite(ticketDetails.getPriorite());

        // Check if a new department ID is provided and update the department
        if (ticketDetails.getDepartementId() != null) {
            Departement departement = departementService.findById(ticketDetails.getDepartementId());
            if (departement == null) {
                return ResponseEntity.badRequest().body(null); // Return a bad request with null body
            }
            ticket.setDepartement(departement);  // Update the department of the ticket
        }

        // Save the updated ticket
        Ticket updatedTicket = ticketService.updateTicket(id, ticket);
        return ResponseEntity.ok(updatedTicket); // Return the updated ticket
    }


    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id).getBody();
        if (ticket != null) {
            return ResponseEntity.ok(ticket); // Retourne le ticket s'il est trouvé
        } else {
            return ResponseEntity.notFound().build(); // Retourne 404 si le ticket n'est pas trouvé
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Ticket>> getTicketsByUtilisateurId(@PathVariable Long id) {
        List<Ticket> tickets = ticketService.getTicketsByUtilisateurId(id);
        if (tickets != null && !tickets.isEmpty()) {
            return ResponseEntity.ok(tickets); // Return tickets if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if no tickets found
        }
    }

//    @GetMapping("/{ticketId}/departement-name")
//    public ResponseEntity<String> getDepartementNameByTicketId(@PathVariable Long ticketId) {
//        Optional<String> departementName = ticketService.getDepartementNameByTicketId(ticketId);
//        if (departementName.isPresent()) {
//            return ResponseEntity.ok(departementName.get()); // Retourne le nom du département si trouvé
//        } else {
//            return ResponseEntity.notFound().build(); // Retourne 404 si le département n'est pas trouvé
//        }
//    }

    @GetMapping("/{ticketId}/departement-name")
    public ResponseEntity<String> getDepartementNameByTicketId(@PathVariable Long ticketId) {
        Optional<String> departementName = ticketService.getDepartementNameByTicketId(ticketId);
        if (departementName.isPresent()) {
            return ResponseEntity.ok(departementName.get()); // Retourne le nom du département si trouvé
        } else {
            return ResponseEntity.notFound().build(); // Retourne 404 si le département n'est pas trouvé
        }
    }


}