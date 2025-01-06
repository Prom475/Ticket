import { Component, OnInit } from '@angular/core';
import { UtilisateurService } from '../services/utilisateur.service';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-utilisateur-list',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule
  ],
  templateUrl: './utilisateur-list.component.html',
  styleUrls: ['./utilisateur-list.component.css'],
})
export class UtilisateurListComponent implements OnInit {
  utilisateurs: any[] = []; // Liste des utilisateurs
  newUtilisateur = { nom: '', email: '', role: 'USER', motDePasse: '' }; // Modèle pour le nouvel utilisateur

  constructor(private utilisateurService: UtilisateurService,
              private router: Router,
              private authService: AuthService,) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    this.fetchUtilisateurs(); // Charger les utilisateurs au démarrage
  }

  fetchUtilisateurs(): void {
    this.utilisateurService.getAllUtilisateurs().subscribe(
      (data: any[]) => {
        this.utilisateurs = data;
      },
      (error: any) => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    );
  }

  // Méthode pour créer un utilisateur
  createUtilisateur(): void {
    if (
      this.newUtilisateur.nom &&
      this.newUtilisateur.email &&
      this.newUtilisateur.motDePasse
    ) {
      this.utilisateurService.createUtilisateur(this.newUtilisateur).subscribe(
        (data: any) => {
          console.log('Utilisateur créé avec succès :', data);
          this.utilisateurs.push(data); // Ajouter l'utilisateur à la liste
          this.newUtilisateur = { nom: '', email: '', role: 'USER', motDePasse: '' }; // Réinitialiser le formulaire
        },
        (error: any) => {
          console.error('Erreur lors de la création de l\'utilisateur :', error);
        }
      );
    } else {
      alert('Veuillez remplir tous les champs obligatoires.');
    }
  }
}
