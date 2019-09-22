import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { IAppConfig } from '../model/config/app-config.model';

/**
 * Service for loading the app configurations.
 */
@Injectable()
export class AppConfig {
  /**
   * App configuration.
   */
  static settings: IAppConfig;

  /**
   * Path to the configuration files.
   */
  private PATH = 'assets/config/config';

  /**
   * Creates a new instance of AppConfig.
   * @param http HttpClient provider.
   */
  constructor(private http: HttpClient) { }

  /**
   * Loads the application configuration.
   */
  load(): Promise<void> {
    const jsonFile = `${this.PATH}.${environment.name}.json`;
    return new Promise<void>((resolve, reject) => {
      this.http.get(jsonFile).toPromise().then((response) => {
        AppConfig.settings = <IAppConfig>response;
        resolve();
      }).catch((response: any) => {
        reject(`Could not load configuration file '${jsonFile}': ${JSON.stringify(response)}`);
      });
    });
  }
}
