import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';  // Importer votre configuration
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .then(() => console.log('Application bootstrap successful!'))
  .catch((err) => console.error('Error during bootstrap:', err));
