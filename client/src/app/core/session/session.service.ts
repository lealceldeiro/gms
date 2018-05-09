import { Injectable } from '@angular/core';
import { User } from './user.model';
import { api } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { LoginResponseBody } from '../../login/shared/response';
import { LoginRequestBody } from '../../login/shared/request';
import { Observable } from 'rxjs/index';

/**
 * A service for providing information about the current session.
 */
@Injectable()
export class SessionService {

  /**
   * Whether the user is logged in or not.
   * @type {boolean}
   */
  public loggedIn = false;

  /**
   * Session user's info (if available).
   */
  public user?: User;

  /**
   * Indicates whether the session info should be kept after the user left the app without login out or not.
   * @type {boolean}
   */
  public rememberMe = false;

  /**
   * API base url.
   */
  private url: string;

  /**
   * Service constructor.
   * @param http HttpClient dependency injection.
   */
  constructor(private http: HttpClient) {
    this.url = api.baseUrl;
  }

  /**
   * Performs a login request.
   * @param {LoginRequestBody} payload Login data to be sent to the service in order to get the authorization data.
   * @returns {Observable<LoginResponseBody>}
   */
  login(payload: LoginRequestBody): Observable<LoginResponseBody> {
    return this.http.post<LoginResponseBody>(this.url + 'login', payload);
  }
}
