import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserRoleService } from '../services/user-role.service';
import { Observable, firstValueFrom } from 'rxjs';

export const roleGuard: CanActivateFn = async (route, state) => {
  const router = inject(Router);
  const userRoleService = inject(UserRoleService);

  // Ensure we have the user role loaded before proceeding
  await firstValueFrom(userRoleService.userRole$);

  const userRole = await firstValueFrom(userRoleService.getUserRole());

  // Check if the role is 'TECHNICIEN'
  if (userRole === 'TECHNICIEN') {
    return true;
  } else {
    router.navigate(['forbidden']); // Redirect to a forbidden page if not a 'TECHNICIEN'
    return false;
  }
};
