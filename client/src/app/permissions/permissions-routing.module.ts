import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PermissionListComponent } from './permission-list/permission-list.component';
import { PermissionInfoComponent } from './permission-info/permission-info.component';

/**
 * Permissions module routes.
 */
const routes = [
  { path: 'list', component: PermissionListComponent },
  { path: 'list/:id', component: PermissionInfoComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class PermissionsRoutingModule { }
