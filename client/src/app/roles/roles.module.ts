import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RolesRoutingModule } from './roles-routing.module';
import { SharedModule } from '../shared/shared.module';
import { RoleListComponent } from './role-list/role-list.component';
import { RolesService } from './shared/roles.service';
import { RoleInfoComponent } from './role-info/role-info.component';

@NgModule({
  imports: [SharedModule, RolesRoutingModule, RouterModule],
  declarations: [RoleListComponent, RoleInfoComponent],
  providers: [RolesService]
})
export class RolesModule { }
