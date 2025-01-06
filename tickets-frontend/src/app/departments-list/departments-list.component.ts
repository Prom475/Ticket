import { Component, OnInit } from '@angular/core';
import { DepartementService } from '../services/departement.service';
import { Departement } from '../models/departement.model';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-departments-list',
  templateUrl: './departments-list.component.html',
  standalone: true,
  imports: [
    NgForOf,
    DatePipe,
    FormsModule,
    NgIf
  ],
  styleUrls: ['./departments-list.component.css']
})
export class DepartmentsListComponent implements OnInit {
  departements: Departement[] = [];
  isCreateFormVisible = false;
  isUpdateFormVisible = false;
  newDepartement: Departement = { nom: '', description: '' };
  selectedDepartement: Departement = { id: 0, nom: '', description: ''};

  constructor(private departementService: DepartementService,
              private authService: AuthService,
              private router: Router) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      console.warn('Utilisateur non authentifié. Redirection vers /login.');
      this.router.navigate(['/login']);
      return;
    }
    this.fetchAllDepartements();
  }

  fetchAllDepartements(): void {
    this.departementService.getAllDepartments().subscribe(
      (departements) => {
        this.departements = departements;
      },
      (error) => {
        console.error('Erreur lors du chargement de tous les départements :', error);
      }
    );
  }

  toggleCreateForm(): void {
    this.isCreateFormVisible = !this.isCreateFormVisible;
    this.newDepartement = { nom: '', description: '' }; // Reset form
  }

  createDepartement(): void {
    this.departementService.addDepartement(this.newDepartement).subscribe(
      (departement) => {
        this.departements.push(departement);
        this.toggleCreateForm(); // Hide form after creation
      },
      (error) => {
        console.error('Erreur lors de la création du département :', error);
      }
    );
  }

  toggleUpdateForm(departement?: Departement): void {
    if (departement) {
      this.selectedDepartement = { ...departement }; // Set selected department to update
    }
    this.isUpdateFormVisible = !this.isUpdateFormVisible;
  }

  updateDepartement(): void {
    this.departementService.updateDepartement(this.selectedDepartement.id, this.selectedDepartement).subscribe(
      (departement) => {
        const index = this.departements.findIndex(d => d.id === departement.id);
        if (index !== -1) {
          this.departements[index] = departement; // Update the department in the list
        }
        this.toggleUpdateForm(); // Hide form after update
      },
      (error) => {
        console.error('Erreur lors de la mise à jour du département :', error);
      }
    );
  }
}
