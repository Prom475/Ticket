import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthService } from './auth.service';
import { UtilisateurService } from './utilisateur.service';

@Injectable({
  providedIn: 'root'
})
export class UserRoleService {
  private userRoleSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  public userRole$: Observable<string | null> = this.userRoleSubject.asObservable();

  constructor(
    private authService: AuthService,
    private utilisateurService: UtilisateurService
  ) {}

  /**
   * Load the user role based on email and store it in the subject.
   */
  loadUserRole(): void {
    if (this.authService.isAuthenticated()) {
      const userEmail = this.authService.getUserEmail();
      if (userEmail) {
        this.utilisateurService.getUtilisateurIdByEmail(userEmail).subscribe(
          (userId) => {
            if (userId) {
              this.authService.getUserRoleById(userId).subscribe(
                (role) => {
                  console.log('Role récupéré:', role);
                  this.userRoleSubject.next(role);  // Update the role
                },
                (error) => {
                  console.error('Erreur lors de la récupération du rôle:', error);
                  this.userRoleSubject.next(null); // Fallback to null if error
                }
              );
            }
          },
          (error) => {
            console.error('Erreur lors de la récupération de l\'ID utilisateur:', error);
            this.userRoleSubject.next(null); // Fallback to null if error
          }
        );
      }
    } else {
      this.userRoleSubject.next(null); // Set to null if not authenticated
    }
  }

  /**
   * Get the user's role.
   * Returns the role stored in the BehaviorSubject.
   */
  getUserRole(): Observable<string | null> {
    return this.userRole$;
  }
}
