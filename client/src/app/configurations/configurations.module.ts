import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConfigurationListComponent } from '../configurations/configuration-list/configuration-list.component';
import { SharedModule } from '../shared/shared.module';
import { ConfigurationsRoutingModule } from './configurations-routing.module';


@NgModule({
  declarations: [ConfigurationListComponent],
  imports: [SharedModule, ConfigurationsRoutingModule, RouterModule]
})
export class ConfigurationsModule { }
