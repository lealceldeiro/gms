import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import * as _ from 'lodash';

/**
 * A service for providing helper functions related to request parameters.
 */
@Injectable({
  providedIn: 'root'
})
export class ParamsService {
  /**
   * Parameter for setting the size of the pages when requesting paginated results.
   */
  public static SIZE = 'size';

  /**
   * Parameter for setting the number of the page to return when requesting paginated results.
   */
  public static PAGE = 'page';

  /**
   * Service constructor.
   */
  constructor() { }

  /**
   * Returns an HttpParams from the given object params.
   * @param params request parameters
   * @return HttpParams
   */
  getHttpParams(params?: { [key: string]: any }): HttpParams {
    let httpParams = new HttpParams();
    for (const k in params) {
      if (_.has(params, k)) {
        httpParams = httpParams.append(k, String(params[k]));
      }
    }
    return httpParams;
  }
}
