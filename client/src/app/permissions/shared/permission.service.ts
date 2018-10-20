import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { PermissionPd } from './permission-pd';
import { environment } from '../../../environments/environment';

/**
 * Service for providing permissions-related services.
 */
@Injectable()
export class PermissionService {

  /**
   * API base url.
   */
  private url = environment.apiBaseUrl + 'permission';

  /**
   * Service's constructor
   * @param {HttpClient} http HttpClient for performing api requests.
   */
  constructor(private http: HttpClient) { }


  /**
   * Returns an observable with a list of permissions.
   * @param {number} size Size of the page for loading permissions.
   * @param {number} page Number of the page.
   * @return {Observable<PermissionPd>} Containing the permissions data.
   */
  getPermissions(size: number, page: number): Observable<PermissionPd> {
    const params = new HttpParams();
    params.append('size', String(size));
    params.append('page', String(page));
    return this.http.get<PermissionPd>(this.url, { params: params });
  }
}
