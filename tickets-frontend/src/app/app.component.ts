import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { UserRoleService } from './services/user-role.service';
import {AuthService} from './services/auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf,
    RouterOutlet
  ],
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'tickets-frontend';
  userRole: string | null = null;

  constructor(
    private userRoleService: UserRoleService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef // Inject ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Load the user role on initialization
    this.userRoleService.loadUserRole();

    // Subscribe to the userRole observable to update the role dynamically
    this.userRoleService.getUserRole().subscribe(role => {
      this.userRole = role;
      this.cdr.detectChanges(); // Manually trigger change detection
    });
  }

  logout(): void {
    this.authService.logout();  // Assuming this method clears the role
    this.router.navigate(['/login']);
  }
}
