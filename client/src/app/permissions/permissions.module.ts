import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/shared.module';
import { PermissionInfoComponent } from './permission-info/permission-info.component';
import { PermissionListComponent } from './permission-list/permission-list.component';
import { PermissionsRoutingModule } from './permissions-routing.module';
import { PermissionService } from './shared/permission.service';

@NgModule({
  imports: [SharedModule, PermissionsRoutingModule, RouterModule],
  providers: [PermissionService],
  declarations: [PermissionListComponent, PermissionInfoComponent]
})
export class PermissionsModule { }
