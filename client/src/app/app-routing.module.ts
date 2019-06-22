import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from './core/guard/login.guard';

const routes: Routes = [
  {
    path: 'login', loadChildren: './login/gms-login.module#GmsLoginModule', canActivateChild: [LoginGuard],
    canLoad: [LoginGuard]
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

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
