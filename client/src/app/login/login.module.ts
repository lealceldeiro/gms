import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login-routing.module';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [ RouterModule,  LoginRoutingModule ],
  declarations: [ LoginComponent ]
})
export class LoginModule { }
