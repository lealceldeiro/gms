import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PermissionListComponent } from './permission-list/permission-list.component';

/**
 * Routes handled in this module.
 * @type {({path: string; component: PermissionListComponent} | {path: string; redirectTo: string; pathMatch: string})[]}
 */
const routes = [
  { path: 'list', component: PermissionListComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
})
export class PermissionsRoutingModule { }
