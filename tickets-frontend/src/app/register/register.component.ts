import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [
    FormsModule,
    NgIf
  ],
  standalone: true
})
export class RegisterComponent {
  user = { nom: '', email: '', motDePasse: '', role: 'USER' }; // Objet pour les champs d'inscription
  errorMessage: string | null = null; // Pour afficher les erreurs

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.register(this.user).subscribe(
      () => {
        this.router.navigate(['/login']); // Redirection vers la page de connexion
      },
      (error: any) => {
        this.errorMessage = "L'inscription a échoué. Essayez à nouveau.";
        console.error(error);
      }
    );
  }
}
