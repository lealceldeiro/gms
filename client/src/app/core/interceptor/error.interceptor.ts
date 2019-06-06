import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
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
   * @param {NotificationService} notificationService Service for showing the messages.
   * @param {InterceptorHelperService} intHelperService for sharing information with the rest of the services.
   */
  constructor(private notificationService: NotificationService, private intHelperService: InterceptorHelperService) { }

  /**
   * Intercepts all responses in order to catch any possible error and take action accordingly.
   * @param {HttpRequest<any>} req Request performed.
   * @param {HttpHandler} next Next http handler.
   * @returns {Observable<HttpEvent<any>>}
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
