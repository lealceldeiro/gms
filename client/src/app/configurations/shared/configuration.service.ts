import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { AppConfig } from '../../core/config/app.config';
import { SessionService } from '../../core/session/session.service';
import { User } from '../../core/session/user.model';

/**
 * Service for providing configurations-related services.
 */
@Injectable()
export class ConfigurationService {

  /**
   * API base url.
   */
  private url = AppConfig.settings.apiServer.url + 'configuration';

  /**
   * Service's constructor
   * @param {HttpClient} http HttpClient for performing api requests.
   */
  constructor(private http: HttpClient, private readonly sessionService: SessionService) { }

  /**
   * Return an Observable of objects. Each object contains all system-wide available configurations.
   * The configurations are in the shape o {configKey: configValue}.
   */
  getConfigurations(): Observable<object> {
    return this.http.get<object>(this.url);
  }

  /**
   * Return an Observable of objects. Each object contains the user available configurations.
   * The configurations are in the shape o {configKey: configValue}.
   * @param humanReadable indicates whether the configuration should show the values to be human readable or not. Default `true`.
   */
  getUserConfigurations(humanReadable = true): Observable<object> {
    return this.sessionService.getUser().pipe(
      switchMap((user: User) => this.http.get<object>(`${this.url}/${user.id}?human=${humanReadable}`))
    );
  }
}
