import { NgModule } from '@angular/core';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';

@NgModule({
  imports: [
    GmsBootstrapComponentsModule, NgbCollapseModule
  ],
  declarations: [],
  exports: [ GmsBootstrapComponentsModule, NgbCollapseModule ]
})
export class SharedModule { }
