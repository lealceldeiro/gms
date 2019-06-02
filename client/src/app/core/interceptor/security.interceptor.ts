import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SessionService } from '../session/session.service';

/**
 * Interceptor for setting on every request made the authorization header if the user has being logged in previously.
 */
@Injectable()
export class SecurityInterceptor implements HttpInterceptor {

  /**
   * Session service for retrieving session-related info in order to modify requests regarding to security parameters.
   */
  private sessionService: SessionService;

  /**
   * Indicates whether the user is logged in or not.
   */
  private isLoggedIn = false;

  /**
   * Holds the authentication header.
   */
  private header: string;

  /**
   * Holds the token type.
   */
  private tokenType: string;

  /**
   * Holds the access token.
   */
  private accessToken: string;


  /**
   * Interceptor constructor.
   * @param {Injector} injector Angular injector for retrieving the service dependencies properly.
   */
  constructor(private injector: Injector) {
    this.sessionService = this.injector.get(SessionService);
    this.sessionService.isLoggedIn().subscribe((ili: boolean) => this.isLoggedIn = ili);
    this.sessionService.getHeader().subscribe((h: string) => this.header = h);
    this.sessionService.getTokenType().subscribe((tt: string) => this.tokenType = tt);
    this.sessionService.getAccessToken().subscribe((at: string) => this.accessToken = at);
  }

  /**
   * Intercepts all request in order to set the Authorization header properly if the user is logged in.
   * @param {HttpRequest<any>} req Request performed.
   * @param {HttpHandler} next Next http handler.
   * @returns {Observable<HttpEvent<any>>}
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.isLoggedIn && this.header && this.tokenType && this.accessToken) { // logged in and all necessary data is here already
      const iHeaders = {};
      iHeaders[this.header] = this.tokenType + ' ' + this.accessToken;
      return next.handle(req.clone({ setHeaders: iHeaders }));
    } else {
      return next.handle(req);
    }
  }
}
