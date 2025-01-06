import {Departement} from './departement.model';
import {Utilisateur} from './utilisateur.model';

export interface Ticket {
  id?: number;
  titre: string;
  description: string;
  statut: string;
  priorite: string;
  dateCreation?: Date;
  dateMiseAJour?: Date;
  departements?: Departement[]; // List of departements related to the ticket
  utilisateurId?: number | null;
  utilisateur?: Utilisateur; // User who created the ticket
  departementId?: number | null; // Autoriser null
}

