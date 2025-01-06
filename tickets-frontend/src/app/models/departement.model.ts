import {Ticket} from './ticket.model';

export interface Departement {
  id?: number;
  nom: string;
  description: string;
  createdAt?: Date;
  ticket?: Ticket;
}
