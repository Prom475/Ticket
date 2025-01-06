package com.gestion.tickets.controller;

import com.gestion.tickets.model.Departement;
import com.gestion.tickets.service.DepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;

    @GetMapping("/ticket/{ticketId}")
    public List<Departement> getDepartementsByTicketId(@PathVariable Long ticketId) {
        return departementService.getDepartementByTicketId(ticketId);
    }

//    @PostMapping("/ticket/{ticketId}")
//    public Departement addDepartement(@PathVariable Long ticketId, @RequestBody Departement departement) {
//        return departementService.addDepartement(ticketId, departement);
//    }

    @PostMapping("/create")
    public Departement addDepartement(@RequestBody Departement departement) {
        return departementService.addDepartement(departement);
    }

    @PutMapping("/{id}")
    public Departement updateDepartement(@PathVariable Long id, @RequestBody Departement departement) {
        return departementService.updateDepartement(id, departement);
    }

    @GetMapping("/all")
    public List<Departement> getAllDepartments() {
        return departementService.getAllDepartments();
    }
}
