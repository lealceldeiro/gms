import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RolesRoutingModule } from './roles-routing.module';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { RoleListComponent } from './role-list/role-list.component';
import { RolesService } from './shared/roles.service';

@NgModule({
  imports: [SharedModule, RolesRoutingModule, RouterModule],
  declarations: [RoleListComponent],
  providers: [RolesService]
})
export class RolesModule { }
