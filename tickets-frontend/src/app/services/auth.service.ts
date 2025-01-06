import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { UtilisateurService } from './utilisateur.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private utilisateurService: UtilisateurService
  ) {}

  /**
   * Méthode de connexion utilisateur
   * @param credentials - Objet contenant email et motDePasse
   * @returns Observable<any>
   */
  login(credentials: { email: string; motDePasse: string }): Observable<any> {
    console.log('Login attempt:', credentials);

    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response.token) {
          console.log('Login successful. Storing token:', response.token);
          localStorage.setItem('token', response.token);
          this.storeConnectedUserId();
        } else {
          console.warn('Login successful but no token received.');
        }
      }),
      catchError((error) => {
        console.error('Login error:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Méthode pour enregistrer un nouvel utilisateur
   * @param user - Objet utilisateur contenant nom, email, motDePasse, etc.
   * @returns Observable<any>
   */
  register(user: {
    nom: string;
    email: string;
    motDePasse: string;
    role?: string;
  }): Observable<any> {
    console.log('Register attempt:', user);

    return this.http.post(`${this.apiUrl}/register`, user).pipe(
      catchError((error) => {
        console.error('Register error:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Déconnexion utilisateur
   */
  logout(): void {
    console.log('User logged out');
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
  }

  /**
   * Vérifie si l'utilisateur est authentifié
   * @returns boolean
   */
  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    console.log('Authentication check. Token exists:', !!token);
    return !!token;
  }

  /**
   * Stocke l'ID de l'utilisateur connecté à partir de son email
   */
  private storeConnectedUserId(): void {
    const email = this.getUserEmail();
    if (email) {
      this.utilisateurService.getUtilisateurIdByEmail(email).subscribe({
        next: (id) => {
          console.log('API returned user ID:', id);
          localStorage.setItem('userId', id.toString());
        },
        error: (err) => {
          console.error('Error fetching user ID:', err);
        },
      });
    } else {
      console.warn('No email found in token, unable to fetch user ID.');
    }
  }

  /**
   * Récupère l'ID de l'utilisateur connecté
   * @returns number | null
   */
  getConnectedUserId(): number | null {
    const userId = localStorage.getItem('userId');
    if (userId) {
      console.log('Connected user ID retrieved from localStorage:', userId);
      return parseInt(userId, 10);
    } else {
      console.warn('No connected user ID found in localStorage');
      return null;
    }
  }

  /**
   * Récupère l'ID de l'utilisateur depuis le token décodé
   * @returns number | null
   */
  getUserId(): number | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.log('Token not found');
      return null;
    }

    try {
      const decodedToken: any = this.decodeToken(token);
      return decodedToken?.id || null;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  /**
   * Récupère l'email de l'utilisateur depuis le token décodé
   * @returns string | null
   */
  getUserEmail(): string | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.warn('No token found in localStorage.');
      return null;
    }

    try {
      const decodedToken: any = this.decodeToken(token);
      return decodedToken?.sub || null;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  /**
   * Vérifie si l'utilisateur a le rôle TECHNICIEN
   * @returns boolean
   */
  isTechnicien(): boolean {
    return this.checkUserRole('TECHNICIEN');
  }

  /**
   * Vérifie si l'utilisateur a le rôle UTILISATEUR
   * @returns boolean
   */
  isUtilisateur(): boolean {
    return this.checkUserRole('UTILISATEUR');
  }

  /**
   * Vérifie le rôle de l'utilisateur
   * @param role - Rôle attendu
   * @returns boolean
   */
  private checkUserRole(role: string): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      console.warn('Token not found');
      return false;
    }

    try {
      const decodedToken: any = this.decodeToken(token);
      console.log('Decoded tokennnnn:', decodedToken);
      return decodedToken?.role === role;
    } catch (error) {
      console.error('Error decoding token:', error);
      return false;
    }
  }

  /**
   * Décode un token JWT
   * @param token - Token JWT
   * @returns any
   */
  private decodeToken(token: string): any {
    const parts = token.split('.');
    if (parts.length !== 3) {
      throw new Error('Invalid token format');
    }
    return JSON.parse(atob(parts[1]));
  }

  /**
   * Récupère le rôle d'un utilisateur via son ID
   * @param userId - ID de l'utilisateur
   * @returns Observable<string | null>
   */
  getUserRoleById(userId: number): Observable<string | null> {
    return this.http.get<{ role: string }>(`${this.apiUrl}/user/${userId}/role`).pipe(
      tap((response) => {
        console.log('User role retrieved:', response.role);
      }),
      map((response) => response.role || null),
      catchError((error) => {
        console.error('Error fetching user role by ID:', error);
        return throwError(() => error);
      })
    );
  }
}
