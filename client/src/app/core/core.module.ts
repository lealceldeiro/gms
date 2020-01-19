import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';

import { FormHelperService } from './form/form-helper.service';
import { LoginGuard } from './guard/login.guard';
import { AllRequestsInterceptor } from './interceptor/all-requests.interceptor';
import { ErrorInterceptor } from './interceptor/error.interceptor';
import { InterceptorHelperService } from './interceptor/interceptor-helper.service';
import { SecurityInterceptor } from './interceptor/security.interceptor';
import { NotificationService } from './messages/notification.service';
import { PageNotFoundService } from './navigation/page-not-found.service';
import { SessionUserService } from './session/session-user.service';
import { SessionService } from './session/session.service';
import { StorageService } from './storage/storage.service';

@NgModule({
  declarations: []
})
export class GmsCoreModule {
  /**
   * Module's constructor.
   * @param parentModule Self GmsCoreModule injected into its own constructor in order to guard against a
   * lazy-loaded module re-importing this module.
   */
  constructor(@Optional() @SkipSelf() parentModule: GmsCoreModule) {
    if (parentModule) {
      console.warn('GmsCoreModule is already loaded. Consider import it in the AppModule if you are only' +
        'using its providers');
    }
  }

  /**
   * Method for getting a module's instance with providers.
   *
   * @param config Optional configuration object with custom providers to be used. The following attributes can be set
   * in the object in order to set custom providers classes:
   * <p>- `notificationService` for providing a class instead of the default NotificationService</p>
   * <p>- `sessionService` for providing a class instead of the default SessionService</p>
   * <p>- `storageService` for providing a class instead of the default StorageService</p>
   * <p>- `loginGuard` for providing a class instead of the default LoginGuard</p>
   * <p>- `sessionUserService` for providing a class instead of the default SessionUserService</p>
   * <p>- `formHelperService` for providing a class instead of the default FormHelperService</p>
   *
   * <p>Example of configuration object: { sessionService: MySessionService } </p>
   */
  static forRoot(config?: any): ModuleWithProviders {
    return {
      ngModule: GmsCoreModule,
      providers: [
        InterceptorHelperService,
        {
          provide: NotificationService,
          useClass: config && config['notificationService'] ? config['notificationService'] : NotificationService
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: SecurityInterceptor,
          multi: true
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AllRequestsInterceptor,
          multi: true
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true
        },
        {
          provide: SessionService,
          useClass: config && config['sessionService'] ? config['sessionService'] : SessionService
        },
        {
          provide: StorageService,
          useClass: config && config['storageService'] ? config['storageService'] : StorageService
        },
        {
          provide: LoginGuard,
          useClass: config && config['loginGuard'] ? config['loginGuard'] : LoginGuard
        },
        {
          provide: SessionUserService,
          useClass: config && config['sessionUserService'] ? config['sessionUserService'] : SessionUserService
        },
        {
          provide: FormHelperService,
          useClass: config && config['formHelperService'] ? config['formHelperService'] : FormHelperService
        },
        PageNotFoundService
      ]
    };
  }
}
