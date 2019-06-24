import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RoleListComponent } from './role-list/role-list.component';

/**
 * Routes handled in this module.
 */
const routes: Routes = [
  { path: 'list', component: RoleListComponent },
  // { path: 'list/:id', component: RoleInfoComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RolesRoutingModule { }
