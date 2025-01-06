package com.gestion.tickets.repository;

import com.gestion.tickets.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    @Query("SELECT d FROM Departement d JOIN d.tickets t WHERE t.id = :ticketId")
    List<Departement> findByTicketId(@Param("ticketId") Long ticketId);
}
