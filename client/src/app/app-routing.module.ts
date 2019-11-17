import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginGuard } from './core/guard/login.guard';

/**
 * Top level routes
 */
export const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./login/gms-login.module').then(mod => mod.GmsLoginModule),
    canActivateChild: [LoginGuard],
    canLoad: [LoginGuard]
  },
  /* {
    path: 'owned-entities',
    loadChildren: () => import('./owned-entities/owned-entities.module').then(mod => mod.OwnedEntitiesModule)
  },
  {
    path: 'users',
    loadChildren: () => import('./users/users.module').then(mod => mod.UsersModule)
  }, */
  {
    path: 'roles',
    loadChildren: () => import('./roles/roles.module').then(mod => mod.RolesModule)
  },
  {
    path: 'permissions',
    loadChildren: () => import('./permissions/permissions.module').then(mod => mod.PermissionsModule)
  },
  {
    path: 'configurations',
    loadChildren: () => import('./configurations/configurations.module').then(mod => mod.ConfigurationsModule)
  },
  {
    path: '**',
    loadChildren: () => import('./page-not-found/page-not-found.module').then(mod => mod.PageNotFoundModule)
  }
];

/**
 * Excluded from menu.
 */
export const excluded = ['login'];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
