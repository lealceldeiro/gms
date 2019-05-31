import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, zip } from 'rxjs/index';

import { SessionService } from '../session/session.service';
import { switchMap } from 'rxjs/internal/operators';

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
   * Interceptor constructor.
   * @param {Injector} injector Angular injector for retrieving the service dependencies properly.
   */
  constructor(private injector: Injector) {
    this.sessionService = this.injector.get(SessionService);
  }

  /**
   * Intercepts all request in order to set the Authorization header properly if the user is logged in.
   * @param {HttpRequest<any>} req Request performed.
   * @param {HttpHandler} next Next http handler.
   * @returns {Observable<HttpEvent<any>>}
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // TODO: rewrite logic. This is somehow storing the requests and calling them all over again!
    const info$ = zip(
      this.sessionService.isLoggedIn(),
      this.sessionService.getHeader(),
      this.sessionService.getTokenType(),
      this.sessionService.getAccessToken()
    );
    return info$.pipe(switchMap(result => {
      if (result[0] && result[1] && result[2] && result[3]) { // logged in and all necessary data is here already
        const iHeaders = {};
        // Auth-header = 'TokenType AccessToken'
        iHeaders[result[1] as string] = result[2] + ' ' + result[3];
        return next.handle(req.clone({ setHeaders: iHeaders}));
      } else {
        return next.handle(req);
      }
    }));
  }
}
