import { NgModule } from '@angular/core';
import { PermissionRoutingModule } from './permission-routing.module';
import { PermissionListComponent } from './permission-list/permission-list.component';

@NgModule({
  imports: [ PermissionRoutingModule ],
  declarations: [ PermissionListComponent ]
})
export class PermissionModule { }
