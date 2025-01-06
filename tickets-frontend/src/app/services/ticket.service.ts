import { Injectable, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { Ticket } from '../models/ticket.model';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private apiUrl = `${environment.apiUrl}/tickets`; // URL de votre API backend

  constructor(private http: HttpClient) {}

  // Récupérer tous les tickets
  getAllTickets(): Observable<any> {
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError<any>('getAllTickets', []))
    );
  }

  // Fonction de gestion des erreurs
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed:`, error); // Log l'erreur ici
      return of(result as T); // Retourne un observable vide en cas d'erreur
    };
  }

  // Suppression d'un ticket
  deleteTicket(id: number): Observable<any> {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce ticket ?')) {
      return this.http.delete<any>(`${this.apiUrl}/${id}`).pipe(
        catchError(this.handleError<any>('deleteTicket'))
      );
    }
    return of(null);
  }

  // Mise à jour d'un ticket
  updateTicket(ticketId: number, ticket: { titre: string; priorite: string; description: string; statut: string }): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${ticketId}`, ticket).pipe(
      catchError(this.handleError<any>('updateTicket'))
    );
  }

  // Récupération d'un ticket par ID
  getTicketById(id: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError<Ticket>('getTicketById'))
    );
  }

  getTicketsByUserId(userId: number): Observable<any[]> {
    console.log('Fetching tickets for user ID:', userId); // Log de l'ID utilisateur
    return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`).pipe(
      tap((response: any[]) => {
        console.log('Tickets received:', response); // Log des tickets reçus
      }),
      catchError((error) => {
        console.error('getTicketsByUserId failed:', error); // Log l'erreur
        console.error('Backend URL:', `${this.apiUrl}/user/${userId}`); // Log de l'URL utilisée
        console.error('Status:', error.status); // Log le status
        return throwError(() => error); // Relancer l'erreur
      })
    );
  }

  // Création d'un ticket
  createTicket(ticket: { titre: string; priorite: string; description: string; statut: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create`, ticket).pipe(
      catchError(this.handleError<any>('createTicket'))
    );
  }

  getAllTicketsWithUsernames(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/with-usernames`).pipe(
      catchError((error) => throwError(() => error))
    );
  }

  getDepartementNameByTicketId(ticketId: number): Observable<string | null> {
    return this.http.get(`${this.apiUrl}/${ticketId}/departement-name`, { responseType: 'text' }).pipe(
      map(response => response || null),
      catchError(error => {
        console.error('Erreur lors de la récupération du nom du département :', error);
        return of(null);
      })
    );
  }
}
