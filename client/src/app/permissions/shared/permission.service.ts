import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { AppConfig } from '../../core/config/app.config';
import { ParamsService } from '../../core/request/params/params.service';
import { RolePd } from '../../roles/shared/role.pd';
import { Permission } from './permission.model';
import { PermissionPd } from './permission.pd';

/**
 * Service for providing permissions-related services.
 */
@Injectable()
export class PermissionService {
  /**
   * API base url.
   */
  private url = AppConfig.settings.apiServer.url + 'permission';

  /**
   * Service's constructor
   * @param http HttpClient for performing api requests.
   * @param paramsService ParamsService for getting request params formatted properly.
   */
  constructor(private http: HttpClient, private paramsService: ParamsService) { }

  /**
   * Returns an observable with a list of permissions.
   * @param size Size of the page for loading permissions.
   * @param page Number of the page starting from 1.
   * @return An Observable<PermissionPd> containing the permissions data.
   */
  getPermissions(size: number, page: number): Observable<PermissionPd> {
    const p: { [key: string]: number } = {};
    p[ParamsService.SIZE] = size;
    p[ParamsService.PAGE] = page - 1;
    return this.http.get<PermissionPd>(this.url, { params: this.paramsService.getHttpParams(p) });
  }

  /**
   * Returns an observable with the info of a permission.
   * @param id Identifier of the permissions whose info is going to be returned.
   */
  getPermissionInfo(id: number): Observable<Permission> {
    return this.http.get<Permission>(`${this.url}/${id}`);
  }

  /**
   * Returns an observable with the roles that use the permission with the same id as the one provided as argument.
   * @param id identifier of the permissions whose roles where it is being used is going to be returned.
   * @param size Size of the page for loading permissions.
   * @param page Number of the page starting from 1.
   * @return An Observable<RolePd> containing the permissions data.
   */
  getPermissionRoles(id: number, size: number = 10, page: number = 1): Observable<RolePd> {
    const p: { [key: string]: number } = {};
    p[ParamsService.SIZE] = size;
    p[ParamsService.PAGE] = page - 1;
    return this.http.get<RolePd>(`${this.url}/${id}/roles`, { params: this.paramsService.getHttpParams(p) });
  }
}
