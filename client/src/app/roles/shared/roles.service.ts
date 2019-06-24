import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ParamsService } from '../../core/request/params/params.service';
import { Role } from './role.model';
import { RolePd } from './role.pd';
import { PermissionPd } from 'src/app/permissions/shared/permission.pd';

/**
 * Service for providing roles-related services.
 */
@Injectable()
export class RolesService {

  /**
   * API base url.
   */
  private url = environment.apiBaseUrl + 'role';

  /**
   * Service's constructor
   * @param {HttpClient} http HttpClient for performing api requests.
   * @param {ParamsService} paramsService ParamsService for getting request params formatted properly.
   */
  constructor(private http: HttpClient, private paramsService: ParamsService) { }

  /**
   * Returns an observable with a list of roles.
   * @param {number} size Size of the page for loading permissions.
   * @param {number} page Number of the page.
   * @return {Observable<RolePd>} Containing the permissions data.
   */
  getRoles(size: number, page: number): Observable<RolePd> {
    const p: { [key: string]: number } = {};
    p[ParamsService.SIZE] = size;
    p[ParamsService.PAGE] = page;
    return this.http.get<RolePd>(this.url, { params: this.paramsService.getHttpParams(p) });
  }

  /**
   * Returns an observable with the info of a role.
   * @param id Identifier of the role whose info is going to be returned.
   */
  getRoleInfo(id: number): Observable<Role> {
    return this.http.get<Role>(`${this.url}/${id}`);
  }

  /**
   * Deletes the info of a role.
   * @param id Identifier of the role whose info is going to be deleted.
   */
  deleteRoleInfo(id: number): Observable<HttpResponse<string>> {
    return this.http.delete(`${this.url}/${id}`, { responseType: 'text', observe: 'response' });
  }

  /**
   * Returns an observable with the permissions assigned to the role with the same id as the one provided as argument.
   * @param id identifier of the role whose permissions are going to be returned.
   */
  getRolePermissions(id: number): Observable<PermissionPd> {
    return this.http.get<PermissionPd>(`${this.url}/${id}/permissions`);
  }
}
