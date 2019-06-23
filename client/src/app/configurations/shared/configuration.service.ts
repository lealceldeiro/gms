import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Service for providing configurations-related services.
 */
@Injectable()
export class ConfigurationService {

  /**
   * API base url.
   */
  private url = environment.apiBaseUrl + 'configuration';

  /**
   * Service's constructor
   * @param {HttpClient} http HttpClient for performing api requests.
   */
  constructor(private http: HttpClient) { }

  /**
   * Return an Observable of objects. Each object contains all system-wide available configurations.
   * The configurations are in the shape o {configKey: configValue}.
   */
  getConfigurations(): Observable<object> {
    return this.http.get<object>(this.url);
  }
}
