import { NgModule } from '@angular/core';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [],
  declarations: [],
  exports: [ CommonModule, FormsModule, GmsBootstrapComponentsModule, NgbCollapseModule ]
})
export class SharedModule { }
