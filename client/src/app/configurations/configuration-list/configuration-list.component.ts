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
  keys: Array<string> = [];

  /**
   * Configurations values corresponding to the keys.
   */
  values: Array<string> = [];

  /**
   * Configurations values' subscription
   */
  configurationValuesSub = new Subscription();

  /**
   * Component constructor
   */
  constructor(private configurationService: ConfigurationService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loadConfigurations();
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.configurationValuesSub.unsubscribe();
  }

  /**
   * Loads the configuration values into the arrays keys and values by calling the method for getting the configuration in
   * the configurations service.
   */
  loadConfigurations(): void {
    this.configurationValuesSub = this.configurationService.getConfigurations().subscribe(configs => {
      this.keys = Object.keys(configs);
      this.values = Object.values(configs);
    });
  }

}
