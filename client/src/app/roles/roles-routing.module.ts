import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RoleInfoComponent } from './role-info/role-info.component';
import { RoleListComponent } from './role-list/role-list.component';

/**
 * Roles module routes.
 */
const routes: Routes = [
  { path: 'list', component: RoleListComponent },
  { path: 'list/:id', component: RoleInfoComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RolesRoutingModule { }
