import { NgModule } from '@angular/core';
import { LoginComponent } from './component/login.component';
import { GmsLoginRoutingModule } from './gms-login-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LoginService } from './service/login.service';

@NgModule({
  imports: [ GmsLoginRoutingModule, SharedModule ],
  declarations: [ LoginComponent ],
  providers: [ LoginService ]
})
export class GmsLoginModule { }
