import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ConfigurationService } from '../shared/configuration.service';

/**
 * A component for showing a list with all the system configurations.
 */
@Component({
  selector: 'gms-configuration-list',
  templateUrl: './configuration-list.component.html',
  styleUrls: ['./configuration-list.component.scss']
})
export class ConfigurationListComponent implements OnInit, OnDestroy {

  /**
   * Configurations key
   */
  keys: { [k: string]: Array<string> } = { system: [], user: []};

  /**
   * Configurations values corresponding to the keys.
   */
  values: { [k: string]: Array<string> } = { system: [], user: []};

  /**
   * Configurations values' subscription
   */
  configurationValuesSub = new Subscription();

  /**
   * User configurations values' subscription
   */
  userConfigurationValuesSub = new Subscription();

  /**
   * Component constructor
   */
  constructor(private configurationService: ConfigurationService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loadConfigurations();
    this.loadUserConfigurations();
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.configurationValuesSub.unsubscribe();
    this.userConfigurationValuesSub.unsubscribe();
  }

  /**
   * Loads the configuration values into the arrays keys and values by calling the method for getting the configuration in
   * the configurations service.
   */
  loadConfigurations(): void {
    this.configurationValuesSub = this.configurationService.getConfigurations().subscribe(configs => {
      this.keys.system = Object.keys(configs);
      this.values.system = Object.values(configs);
    });
  }

  /**
   * Loads the configuration values related to the logged in user into the arrays keys and values..
   */
  loadUserConfigurations(): void {
    this.userConfigurationValuesSub = this.configurationService.getUserConfigurations().subscribe(configs => {
      this.keys.user = Object.keys(configs);
      this.values.user = Object.values(configs);
    });
  }

}
