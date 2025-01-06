import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UtilisateurService {
  private apiUrl = `${environment.apiUrl}/utilisateurs`; // URL de l'API backend

  constructor(private http: HttpClient) {}

  getAllUtilisateurs(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getUtilisateurByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/email/${email}`);
  }

  getUtilisateurIdByEmail(email: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/idByEmail/${email}`);
  }

  createUtilisateur(utilisateur: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, utilisateur);
  }
}
