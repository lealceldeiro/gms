import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/index';
import { tap } from 'rxjs/operators';

import { AppConfig } from '../../core/config/app.config';
import { InterceptorHelperService } from '../../core/interceptor/interceptor-helper.service';
import { UserPdModel } from '../../core/response/paginated-data/impl/user-pd-.model';
import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { SessionUserService } from '../../core/session/session-user.service';
import { SessionService } from '../../core/session/session.service';

/**
 * A service for providing handling the login/logout processes.
 */
@Injectable()
export class LoginService {

  /**
   * API base url.
   */
  private url = AppConfig.settings.apiServer.url;
  /**
   * Login endpoint.
   */
  private loginUrl = AppConfig.settings.apiServer.loginUrl;

  /**
   * Service constructor.
   * @param http HttpClient for making http requests.
   * @param sessionService SessionService for storing/retrieving session-related information.
   * @param sessionUserService SessionUserService for storing/retrieving session user-related information.
   * @param intHelperService InterceptorHelperService for sharing information with the interceptors.
   */
  constructor(private http: HttpClient, private sessionService: SessionService,
    private sessionUserService: SessionUserService, private intHelperService: InterceptorHelperService) {
    this.intHelperService.addExcludedFromErrorHandling(this.loginUrl);
  }

  /**
   * Performs a login request.
   * @param {LoginResponseModel} payload Login data to be sent to the service in order to get the authorization data.
   * @returns {Observable<LoginResponseModel>}
   */
  login(payload: LoginRequestModel): Observable<LoginResponseModel> {
    return this.http.post<LoginResponseModel>(this.url + this.loginUrl, payload).pipe(
      tap((response) => {
        if (response.access_token && response.username) {
          this.sessionService.setAuthData(response);
          this.sessionService.setLoggedIn(true);
          this.sessionUserService.getCurrentUser(response.username).subscribe((userPgData: UserPdModel) => {
            this.sessionService.setUser(userPgData._embedded.user[0]);
          });
        }
      })
    );
  }
}
