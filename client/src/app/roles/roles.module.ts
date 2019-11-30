import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/shared.module';
import { RoleInfoComponent } from './role-info/role-info.component';
import { RoleListComponent } from './role-list/role-list.component';
import { RolesRoutingModule } from './roles-routing.module';
import { RolesService } from './shared/roles.service';

@NgModule({
  imports: [SharedModule, RolesRoutingModule, RouterModule],
  declarations: [RoleListComponent, RoleInfoComponent],
  providers: [RolesService]
})
export class RolesModule { }
