import { Injectable } from '@angular/core';
import { User } from './user.model';
import { api } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { LoginResponseBody } from '../../login/shared/response';
import { LoginRequestBody } from '../../login/shared/request';
import { Observable } from 'rxjs/index';
import { StorageService } from '../storage/storage.service';
import { tap } from 'rxjs/operators';

/**
 * A service for providing information about the current session.
 */
@Injectable()
export class SessionService {

  /**
   * Key under which the value `loggedIn` is stored in the StorageService.
   * @type {string}
   */
  private loggedInKey = 'loggedIn';

  /**
   * Whether the user is logged in or not.
   * @type {boolean}
   */
  public loggedIn: boolean;

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
   * @param storageService StorageService for storing session-related information.
   */
  constructor(private http: HttpClient, private storageService: StorageService) {
    this.url = api.baseUrl;
  }

  /**
   * Performs a login request.
   * @param {LoginRequestBody} payload Login data to be sent to the service in order to get the authorization data.
   * @returns {Observable<LoginResponseBody>}
   */
  login(payload: LoginRequestBody): Observable<LoginResponseBody> {
    return this.http.post<LoginResponseBody>(this.url + 'login', payload).pipe(
      tap((response) => {
        if (response.access_token) {
          this.loggedIn = true;
          this.storageService.set(this.loggedInKey, this.loggedIn);
        }
      })
    );
  }
}
