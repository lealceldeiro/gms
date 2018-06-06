import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/operators';
import { HTTP_STATUS_UNAUTHORIZED } from '../response/http-status';
import { ToastrService } from 'ngx-toastr';
import { InterceptorHelperService } from './interceptor-helper.service';

/**
 * Interceptor for catching all response errors and take action according to every specific error.
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  /**
   * Interceptor constructor.
   * @param toastr {ToastrService} Service for showing the messages.
   * @param intHelperService InterceptorHelperService for sharing information with the rest of the services.
   */
  constructor(private toastr: ToastrService, private intHelperService: InterceptorHelperService) { }

  /**
   * Intercepts all responses in order to catch any possible error and take action accordingly.
   * @param {HttpRequest<any>} req Request performed.
   * @param {HttpHandler} next Next http handler.
   * @returns {Observable<HttpEvent<any>>}
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.intHelperService.isExcludedFromErrorHandling(req.url)) {
      return next.handle(req).pipe(tap(
        () => {},
        (event) => {
          if (event instanceof HttpErrorResponse) {
            let message = '';
            if (event.error) {
              if (event.error['error']) {
                message += event.error['error'] + ': ';
              }
              if (event.error['message']) {
                message += event.error['message'];
              }
            }
            switch (event.status) {
              case HTTP_STATUS_UNAUTHORIZED:
                this.toastr.error(message, 'Unauthorized');
                break;
            }
          }
        }
      ));
    } else {
      return next.handle(req);
    }
  }
}
