import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PermissionListComponent } from './permission-list/permission-list.component';

const routes = [
  { path: 'list', component: PermissionListComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
})
export class PermissionRoutingModule { }
