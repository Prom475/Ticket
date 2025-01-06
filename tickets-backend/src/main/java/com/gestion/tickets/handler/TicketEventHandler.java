//package com.gestion.tickets.handler;
//
//import org.springframework.data.rest.core.annotation.HandleAfterCreate;
//import org.springframework.data.rest.core.annotation.HandleBeforeSave;
//import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
//import org.springframework.stereotype.Component;
//import com.gestion.tickets.model.Ticket;
//import java.time.LocalDateTime;
//
//@Component
//@RepositoryEventHandler(Ticket.class)
//public class TicketEventHandler {
//
//    @HandleBeforeSave
//    public void handleBeforeSave(Ticket ticket) {
//        // Logique exécutée avant de sauvegarder un ticket
//        ticket.setUpdatedAt(LocalDateTime.now());
//        System.out.println("Mise à jour du ticket : " + ticket.getTitle());
//    }
//
//    @HandleAfterCreate
//    public void handleAfterCreate(Ticket ticket) {
//        // Logique exécutée après la création d'un ticket
//        System.out.println("Nouveau ticket créé : " + ticket.getTitle());
//    }
//}
