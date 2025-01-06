import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Departement } from '../models/departement.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DepartementService {
  private apiUrl = `${environment.apiUrl}/departements`;

  constructor(private http: HttpClient) {}

  getDepartements(ticketId: number): Observable<Departement[]> {
    return this.http.get<Departement[]>(`${this.apiUrl}/ticket/${ticketId}`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération des départements :', error);
        return throwError(error); // Rejette l'erreur pour la gérer ailleurs
      })
    );
  }

  getAllDepartments(): Observable<Departement[]> {
    return this.http.get<Departement[]>(`${this.apiUrl}/all`).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération de tous les départements :', error);
        return throwError(error); // Rejette l'erreur pour la gérer ailleurs
      })
    );
  }

  addDepartement(departement: Departement): Observable<Departement> {
    return this.http.post<Departement>(`${this.apiUrl}/create`, departement).pipe(
      catchError(error => {
        console.error('Erreur lors de la création du département :', error);
        return throwError(error); // Rejette l'erreur pour la gérer ailleurs
      })
    );
  }

  updateDepartement(id: number | undefined, departement: Departement): Observable<Departement> {
    return this.http.put<Departement>(`${this.apiUrl}/${id}`, departement).pipe(
      catchError(error => {
        console.error('Erreur lors de la mise à jour du département :', error);
        return throwError(error); // Rejette l'erreur pour la gérer ailleurs
      })
    );
  }
}
