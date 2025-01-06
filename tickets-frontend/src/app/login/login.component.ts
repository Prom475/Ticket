import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [
    NgIf,
    FormsModule
  ],
  standalone: true
})
export class LoginComponent {
  credentials = { email: '', motDePasse: '' }; // Objet pour les champs de connexion
  errorMessage: string | null = null; // Pour afficher les erreurs

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.credentials).subscribe(
      () => {
        // Rediriger vers '/tickets' après connexion réussie
        this.router.navigate(['/tickets']);
      },
      (error: any) => {
        this.errorMessage = 'Échec de la connexion. Vérifiez vos informations.';
        console.error('Login error:', error);
      }
    );
  }
}
