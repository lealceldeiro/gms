import { NgModule } from '@angular/core';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [],
  declarations: [],
  exports: [ CommonModule, FormsModule, ReactiveFormsModule, GmsBootstrapComponentsModule, NgbCollapseModule ]
})
export class SharedModule { }
