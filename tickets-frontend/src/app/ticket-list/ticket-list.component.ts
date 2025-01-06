import { Component, OnInit } from '@angular/core';
import { TicketService } from '../services/ticket.service';
import { AuthService } from '../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import { UtilisateurService } from '../services/utilisateur.service';
import {DatePipe, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  standalone: true,
  imports: [NgIf, NgForOf, RouterLink, DatePipe],
  styleUrls: ['./ticket-list.component.css'],
})
export class TicketListComponent implements OnInit {
  tickets: any[] = [];
  userId: number | null = null;
  userEmail: string | null = null;
  userRole: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private ticketService: TicketService,
    private authService: AuthService,
    private utilisateurService: UtilisateurService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      console.warn('Utilisateur non authentifié. Redirection vers /login.');
      this.router.navigate(['/login']);
      return;
    }

    this.userEmail = this.authService.getUserEmail(); // Obtient l'email de l'utilisateur authentifié

    if (this.userEmail) {
      // Récupération de l'ID utilisateur
      this.utilisateurService.getUtilisateurIdByEmail(this.userEmail).subscribe(
        (id: number) => {
          this.userId = id;
          console.log('Utilisateur connecté ID :', this.userId);
          this.loadUserRole(); // Charger les tickets selon le rôle utilisateur
        },
        (error: any) => {
          console.error('Erreur lors de la récupération de l\'ID utilisateur :', error);
          this.errorMessage = 'Erreur lors de la récupération de l\'ID utilisateur.';
        }
      );
    }
  }

  private loadUserRole(): void {
    if (!this.userId) return;
    this.authService.getUserRoleById(this.userId).subscribe(
      (role: string | null) => {
        if (role) {
          this.userRole = role;
          console.log('Utilisateur connecté Role :', this.userRole);
          if (this.userRole === 'USER') {
            this.loadUserTickets();
          } else if (this.userRole === 'TECHNICIEN') {
            this.loadAllTicketsWithUserEmail();
          } else {
            console.warn('Rôle inconnu. Aucun ticket chargé.');
          }
        } else {
          console.error('Erreur : rôle utilisateur introuvable.');
          this.errorMessage = 'Erreur : rôle utilisateur introuvable.';
        }
      },
      (error: any) => {
        console.error('Erreur lors de la récupération du rôle utilisateur :', error);
        this.errorMessage = 'Erreur lors de la récupération du rôle utilisateur.';
      }
    );
  }

  loadUserTickets(): void {
    if (!this.userId) return;
    this.ticketService.getTicketsByUserId(this.userId).subscribe({
      next: (data: any[]) => {
        this.tickets = data.map(ticket => ({
          ...ticket,
          utilisateurNom: ticket.utilisateur?.nom,
          utilisateurEmail: ticket.utilisateur?.email,
          departementNom: ''
        }));

        // Récupération du nom du département pour chaque ticket
        this.tickets.forEach(ticket => {
          this.ticketService.getDepartementNameByTicketId(ticket.id).subscribe({
            next: (departementNom: string | null) => {
              console.log('Nom du département reçu :', departementNom);
              ticket.departementNom = departementNom || 'Aucun département';
            },
            error: (error: any) => {
              console.error(`Erreur détaillée pour le ticket ID: ${ticket.id}`, error);
              ticket.departementNom = 'Erreur de récupération';
            }
          });
        });
      },
      error: (error: any) => {
        console.error('Erreur complète lors de la récupération des tickets :', error);
        this.errorMessage = 'Erreur lors de la récupération des tickets utilisateur.';
      }
    });
  }
  private loadAllTicketsWithUserEmail(): void {
    this.ticketService.getAllTickets().subscribe(
      (data: any[]) => {
        console.log('Tous les tickets reçus :', data);
        this.tickets = data.map(ticket => ({
          ...ticket,
          utilisateurNom: ticket.utilisateur?.nom,
          utilisateurEmail: ticket.utilisateur?.email,
          departementNom: '' // Récupération du nom du département avec une méthode dédiée
        }));

        // Récupération du nom du département pour chaque ticket
        this.tickets.forEach(ticket => {
          this.ticketService.getDepartementNameByTicketId(ticket.id).subscribe(
            (departementNom: string | null) => {
              ticket.departementNom = departementNom || '';
            },
            (error: any) => {
              console.error(`Erreur lors de la récupération du département pour le ticket ID: ${ticket.id}`, error);
            }
          );
        });
      },
      (error: any) => {
        console.error('Erreur lors de la récupération de tous les tickets.', error);
        this.errorMessage = 'Erreur lors de la récupération de tous les tickets.';
      }
    );
  }

  editTicket(id: number): void {
    this.router.navigate(['/tickets', id]);
  }

  deleteTicket(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce ticket ?')) {
      this.ticketService.deleteTicket(id).subscribe(
        () => {
          this.tickets = this.tickets.filter(ticket => ticket.id !== id);
        },
        (error: any) => {
          console.error('Erreur lors de la suppression du ticket :', error);
          this.errorMessage = 'Erreur lors de la suppression du ticket.';
        }
      );
    }
  }
}
