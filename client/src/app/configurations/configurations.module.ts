import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigurationsRoutingModule } from './configurations-routing.module';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [SharedModule, ConfigurationsRoutingModule, RouterModule]
})
export class ConfigurationsModule { }
