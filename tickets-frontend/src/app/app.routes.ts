import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TicketListComponent } from './ticket-list/ticket-list.component';
import { TicketFormComponent } from './ticket-form/ticket-form.component';
import {UtilisateurListComponent} from './utilisateur-list/utilisateur-list.component';
import {RegisterComponent} from './register/register.component';
import {LoginComponent} from './login/login.component';
import {DepartmentsListComponent} from './departments-list/departments-list.component';
import {roleGuard} from './guards/role.guard';
import {ForbiddenComponent} from './forbidden/forbidden.component';

// Définition des routes
export const routes: Routes = [
  { path: 'tickets', component: TicketListComponent },// Liste des tickets
  {path : 'utilisateur',component : UtilisateurListComponent, canActivate : [roleGuard]},
  {path : 'departments',component : DepartmentsListComponent, canActivate : [roleGuard]},
  { path: 'tickets/new', component: TicketFormComponent },  // Formulaire pour ajouter un ticket
  { path: 'tickets/:id', component: TicketFormComponent },  // Formulaire pour modifier un ticket
  { path: 'utilisateur', component: UtilisateurListComponent },
  { path: 'departments', component: DepartmentsListComponent }, // Nouvelle route pour la liste des départements
  { path: 'login', component: LoginComponent },
  {path : "forbidden",component : ForbiddenComponent},
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }   // Redirection vers la liste des tickets par défaut
];

@NgModule({
  imports: [RouterModule.forRoot(routes)], // Configuration des routes
  exports: [RouterModule]
})
export class AppRoutingModule {}
