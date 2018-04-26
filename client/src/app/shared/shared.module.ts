import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule, GmsBootstrapComponentsModule, NgbCollapseModule, FormsModule
  ],
  declarations: [],
  exports: [
    CommonModule, GmsBootstrapComponentsModule, NgbCollapseModule, FormsModule
  ]
})
export class SharedModule { }
