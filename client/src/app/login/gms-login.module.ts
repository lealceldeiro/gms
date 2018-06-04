import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { LoginComponent } from './component/login.component';
import { GmsLoginRoutingModule } from './gms-login-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LoginService } from './service/login.service';

@NgModule({
  imports: [ NgbModule, GmsLoginRoutingModule, SharedModule ],
  declarations: [ LoginComponent ],
  providers: [ LoginService ]
})
export class GmsLoginModule { }
