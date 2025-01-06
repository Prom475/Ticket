import { DepartementService } from '../services/departement.service'; // Import service
import { Departement } from '../models/departement.model';
import {Ticket} from '../models/ticket.model';
import {FormsModule, NgForm} from '@angular/forms';
import {TicketService} from '../services/ticket.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {UtilisateurService} from '../services/utilisateur.service';
import {NgForOf, NgIf} from '@angular/common';
import {Component, OnInit} from '@angular/core'; // Import modèle Departement

@Component({
  selector: 'app-ticket-form',
  templateUrl: 'ticket-form.component.html',
  standalone: true,
  imports: [FormsModule, NgIf, NgForOf],
  styleUrls: ['./ticket-form.component.css']
})
export class TicketFormComponent implements OnInit {
  ticket: Ticket = {
    titre: '',
    description: '',
    statut: '',
    priorite: '',
    utilisateurId: null,
    departementId: null // Ajout du champ departementId
  };
  departements: Departement[] = []; // Liste des départements disponibles
  ticketId: number | null = null;
  errorMessage: string | null = null;
  userId: number | null = null;
  userEmail: string | null = null;

  constructor(
    private ticketService: TicketService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private utilisateurService: UtilisateurService,
    private departementService: DepartementService // Inject DepartementService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.userEmail = this.authService.getUserEmail();

    if (this.userEmail) {
      this.utilisateurService.getUtilisateurIdByEmail(this.userEmail).subscribe(
        (id: number) => {
          this.userId = id;
          this.ticket.utilisateurId = this.userId;
        },
        (error: any) => {
          console.error('Erreur lors de la récupération de l\'ID utilisateur :', error);
        }
      );
    }

    // Charger les départements
    this.loadDepartements();

    // Vérifier si un ticket doit être modifié
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.ticketId = +id;
        this.getTicket(this.ticketId);
      }
    });
  }

  loadDepartements(): void {
    this.departementService.getAllDepartments().subscribe(
      (data: Departement[]) => {
        this.departements = data; // Charger la liste des départements
      },
      (error) => {
        console.error('Erreur lors du chargement des départements :', error);
      }
    );
  }

  getTicket(id: number): void {
    this.ticketService.getTicketById(id).subscribe(
      (data: Ticket) => {
        this.ticket = data;
        // La valeur departementId est déjà récupérée avec le ticket
        // Pas besoin de méthode supplémentaire pour charger le département
      },
      (error) => {
        this.errorMessage = 'Erreur lors du chargement du ticket.';
        console.error('Erreur lors du chargement du ticket :', error);
      }
    );
  }


  saveTicket(form: NgForm): void {
    if (form.invalid) {
      this.errorMessage = 'Veuillez corriger les erreurs de formulaire.';
      return;
    }

    if (this.ticketId) {
      this.ticketService.updateTicket(this.ticketId, this.ticket).subscribe(
        () => {
          this.router.navigate(['/tickets']);
        },
        (error) => {
          this.errorMessage = 'Erreur lors de la mise à jour du ticket.';
          console.error('Erreur lors de la mise à jour du ticket :', error);
        }
      );
    } else {
      this.ticketService.createTicket(this.ticket).subscribe(
        () => {
          this.router.navigate(['/tickets']);
        },
        (error) => {
          this.errorMessage = 'Erreur lors de la création du ticket.';
          console.error('Erreur lors de la création du ticket :', error);
        }
      );
    }
  }
}
