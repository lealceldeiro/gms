import { NgModule } from '@angular/core';
import { PermissionsRoutingModule } from './permissions-routing.module';
import { PermissionListComponent } from './permission-list/permission-list.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [ SharedModule, PermissionsRoutingModule ],
  declarations: [ PermissionListComponent ]
})
export class PermissionsModule { }
