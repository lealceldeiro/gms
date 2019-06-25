import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbCollapseModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';
import { BooleanPipe } from './boolean-pipe/boolean.pipe';

@NgModule({
  imports: [],
  declarations: [BooleanPipe],
  exports: [CommonModule, FormsModule, ReactiveFormsModule, GmsBootstrapComponentsModule, NgbCollapseModule, NgbModule, BooleanPipe]
})
export class SharedModule { }
