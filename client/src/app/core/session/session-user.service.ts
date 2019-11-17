import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { AppConfig } from '../config/app.config';
import { UserPdModel } from '../response/paginated-data/impl/user-pd-.model';

/**
 * Service for providing all application-wide user-related services.
 */
@Injectable()
export class SessionUserService {
  /**
   * API base url.
   */
  private url = AppConfig.settings.apiServer.url;

  /**
   * Service's constructor
   * @param {HttpClient} http HttpClient for performing api requests.
   */
  constructor(private http: HttpClient) { }

  /**
   * Gets the information related to the user active in the current session.
   * @param {string} usernameOrEmail User's email or username
   * @returns {Observable<UserPdModel>}
   */
  getCurrentUser(usernameOrEmail: string): Observable<UserPdModel> {
    return this.http.get<UserPdModel>(this.url + 'user/search/username-email',
      { params: { username: usernameOrEmail, email: usernameOrEmail } });
  }
}
