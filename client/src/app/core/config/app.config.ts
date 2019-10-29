import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import * as _ from 'lodash';

import { environment } from '../../../environments/environment';
import { IAppConfig } from '../model/config/app-config.model';
import { Util } from '../util/util';

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
  private readonly PATH = `assets/config/config.${environment.name}.json`;

  /**
   * Creates a new instance of AppConfig.
   * @param http HttpClient provider.
   */
  constructor(private http: HttpClient) { }

  /**
   * Loads the application configuration.
   */
  load(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.http.get<IAppConfig>(this.PATH).toPromise()
        .then((appConfig: IAppConfig) => {
          const keyHashed = Util.hashMapFrom(appConfig.security.hash.key);
          AppConfig.settings = { ...appConfig, security: { hash: { key: keyHashed } } };

          resolve();
        })
        .catch((err: any) => {
          reject(`Could not load configuration file '${this.PATH}': ${_(err).value()}`);
        });
    });
  }
}
