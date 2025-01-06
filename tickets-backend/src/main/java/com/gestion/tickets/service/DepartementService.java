package com.gestion.tickets.service;

import com.gestion.tickets.model.Departement;
import com.gestion.tickets.model.Ticket;
import com.gestion.tickets.repository.DepartementRepository;
import com.gestion.tickets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {
    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Récupère les départements associés à un ticket spécifique.
     *
     * @param ticketId L'identifiant du ticket.
     * @return Liste des départements associés.
     */
    public List<Departement> getDepartementByTicketId(Long ticketId) {
        return departementRepository.findByTicketId(ticketId);
    }

    /**
     * Ajoute un département sans nécessiter de ticketId dans l'URL.
     * Ce service crée un département et l'associe à un ticket existant.
     *
     * @param departement Le département à ajouter (avec un ticket déjà associé).
     * @return Le département ajouté.
     */
    public Departement addDepartement(Departement departement) {
        // Définir la date de création automatiquement
        departement.setCreatedAt(LocalDateTime.now());
        // Enregistrer le département
        return departementRepository.save(departement);
    }


    /**
     * Met à jour un département existant.
     *
     * @param id L'identifiant du département à mettre à jour.
     * @param departement Le département contenant les nouvelles informations.
     * @return Le département mis à jour.
     */
    public Departement updateDepartement(Long id, Departement departement) {
        Departement existingDepartement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département not found"));

        existingDepartement.setNom(departement.getNom());
        existingDepartement.setDescription(departement.getDescription());

        return departementRepository.save(existingDepartement);
    }

    /**
     * Récupère tous les départements.
     *
     * @return Liste de tous les départements.
     */
    public List<Departement> getAllDepartments() {
        return departementRepository.findAll();
    }

    public Departement findById(Long departementId) {
        Optional<Departement> departementOpt = departementRepository.findById(departementId);
        return departementOpt.orElseThrow(() -> new RuntimeException("Département non trouvé"));
    }
}
