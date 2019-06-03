import { NgModule } from '@angular/core';
import { PermissionsRoutingModule } from './permissions-routing.module';
import { PermissionListComponent } from './permission-list/permission-list.component';
import { SharedModule } from '../shared/shared.module';
import { PermissionService } from './shared/permission.service';

@NgModule({
  imports: [SharedModule, PermissionsRoutingModule],
  providers: [PermissionService],
  declarations: [PermissionListComponent]
})
export class PermissionsModule { }
