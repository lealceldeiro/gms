import { NgModule } from '@angular/core';

import { SharedModule } from '../shared/shared.module';
import { ConfigurationListComponent } from './configuration-list/configuration-list.component';
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
export class ConfigurationsModule {
}
