import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BooleanPipe } from './boolean-pipe/boolean.pipe';
import { BootstrapComponentsModule } from './bootstrap-components/bootstrap-components.module';

@NgModule({
  declarations: [BooleanPipe],
  exports: [CommonModule, FormsModule, ReactiveFormsModule, BootstrapComponentsModule, BooleanPipe]
})
export class SharedModule { }
