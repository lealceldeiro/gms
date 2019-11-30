import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable } from 'rxjs/';
import { tap } from 'rxjs/operators';

import { NotificationService } from '../messages/notification.service';
import { HttpStatusCode } from '../response/http-status-code.enum';
import { InterceptorHelperService } from './interceptor-helper.service';

/**
 * Interceptor for catching all response errors and take action according to every specific error.
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  /**
   * Interceptor constructor.
   *
   * @param notificationService Service for showing the messages.
   * @param intHelperService for sharing information with the rest of the services.
   * @param router fir navigating to home page when there is a not found error.
   */
  constructor(
    private notificationService: NotificationService,
    private intHelperService: InterceptorHelperService,
    private router: Router
  ) { }

  /**
   * Intercepts all responses in order to catch any possible error and take action accordingly.
   *
   * @param req Request performed.
   * @param next Next http handler.
   * @returns Observable<HttpEvent<any>>
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(tap(
      () => { },
      (event) => {
        if (!this.intHelperService.isExcludedFromErrorHandling(req.url) && event instanceof HttpErrorResponse) {
          let message = '';
          let title = 'Error';
          if (event.error) {
            if (event.error['error']) {
              message += event.error['error'] + ': ';
            }
            if (event.error['message']) {
              message += event.error['message'];
            }
          }
          switch (event.status) {
            case HttpStatusCode.UNAUTHORIZED:
              title = 'Unauthorized';
              message = message || 'You don\'t have permission to access this resource';
              this.router.navigateByUrl('/');
              break;
            case HttpStatusCode.NOT_FOUND:
              title = 'Not found';
              message = message || 'Resource not found';
              this.router.navigateByUrl('/');
              break;
            default:
              break;
          }
          this.notificationService.error(message, title);
        }
      }
    ));
  }
}
