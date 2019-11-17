import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';

import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

/**
 * Interceptor for setting/un-setting the loading indicator while doing all requests to the back-end API.
 */
@Injectable()
export class AllRequestsInterceptor implements HttpInterceptor {
  /**
   * Service which handles the UI loader.
   * @type {NgxUiLoaderService}
   */
  private loaderService: NgxUiLoaderService;

  /**
   * Number of pending requests.
   */
  private requests = 0;

  /**
   * Indicates whether the loader is started or not.
   */
  private started = false;

  /**
   * Interceptor constructor.
   * @param {Injector} injector Angular injector for retrieving the service dependencies properly.
   */
  constructor(private injector: Injector) {
    this.loaderService = this.injector.get(NgxUiLoaderService);
  }

  /**
   * Intercepts all requests responses for setting/un-setting the loading indicator while doing all requests to the backend API.
   * @param {HttpRequest<any>} req Request performed.
   * @param {HttpHandler} next Next http handler.
   * @returns {Observable<HttpEvent<any>>}
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.startRequest();
    return next.handle(req).pipe(tap(
      (res) => {
        if (res instanceof HttpResponse) {
          this.stopRequest();
        }
      },
      () => this.stopRequest(),
      () => this.stopRequest()
    ));
  }

  /**
   * Increases the requests count and starts the loader indicator (if not already started).
   */
  private startRequest(): void {
    if (!this.started) {
      this.started = true;
      this.loaderService.start();
    }
    this.requests++;
  }

  /**
   * Decreases the requests count and stop the loader indicator if there are not more pending requests.
   */
  private stopRequest(): void {
    if (--this.requests <= 0) {
      this.started = false;
      this.requests = 0;
      this.loaderService.stopAll();
    }
  }
}
