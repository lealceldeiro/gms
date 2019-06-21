import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ParamsService } from '../../core/request/params/params.service';
import { PermissionPd } from './permission.pd';
import { Permission } from './permission.model';

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
   * @param {ParamsService} paramsService ParamsService for getting request params formatted properly.
   */
  constructor(private http: HttpClient, private paramsService: ParamsService) { }

  /**
   * Returns an observable with a list of permissions.
   * @param {number} size Size of the page for loading permissions.
   * @param {number} page Number of the page.
   * @return {Observable<PermissionPd>} Containing the permissions data.
   */
  getPermissions(size: number, page: number): Observable<PermissionPd> {
    const p: { [key: string]: number } = {};
    p[ParamsService.SIZE] = size;
    p[ParamsService.PAGE] = page;
    return this.http.get<PermissionPd>(this.url, { params: this.paramsService.getHttpParams(p) });
  }

  /**
   * Returns an observable with the info of a permission.
   * @param id Identifier of the permissions whose info is going to be returned.
   */
  getPermissionInfo(id: number): Observable<Permission> {
    return this.http.get<Permission>(this.url + '/' + id);
  }

}
