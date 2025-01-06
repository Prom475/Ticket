export interface Utilisateur {
  id?: number;
  nom: string;
  email: string;
  role: string; // Example: "ADMIN", "TECHNICIEN", "UTILISATEUR"
  motDePasse: string;
}
