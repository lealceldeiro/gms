import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { GmsLoginRoutingModule } from './gms-login-routing.module';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [ GmsLoginRoutingModule, SharedModule ],
  declarations: [ LoginComponent ]
})
export class GmsLoginModule { }
