import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ParamsService } from 'src/app/core/request/params/params.service';
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
   * @param {ParamsService} paramsService ParamsService for getting request params formatted properly.
   */
  constructor(private http: HttpClient, private paramsService: ParamsService) { }

  /**
   * Return an Observable of objects. Each object contains all system-wide available configurations.
   * The configurations are in the shape o {configKey: configValue}.
   */
  getConfigurations(): Observable<object> {
    return this.http.get<object>(this.url);
  }
}
