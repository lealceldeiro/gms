import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BooleanPipe } from './boolean-pipe/boolean.pipe';
import { GmsBootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';

@NgModule({
  imports: [],
  declarations: [BooleanPipe],
  exports: [CommonModule, FormsModule, ReactiveFormsModule, GmsBootstrapComponentsModule, BooleanPipe]
})
export class SharedModule { }
