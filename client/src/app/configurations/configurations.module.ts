import { NgModule } from '@angular/core';

import { ConfigurationListComponent } from '../configurations/configuration-list/configuration-list.component';
import { SharedModule } from '../shared/shared.module';
import { ConfigurationsRoutingModule } from './configurations-routing.module';
import { ConfigurationService } from './shared/configuration.service';

/**
 * Module holding all components which handles the server configuration resources.
 */
@NgModule({
  declarations: [ConfigurationListComponent],
  imports: [SharedModule, ConfigurationsRoutingModule],
  providers: [ConfigurationService]
})
export class ConfigurationsModule { }
