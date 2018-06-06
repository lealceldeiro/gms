import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { SessionService } from './session/session.service';
import { StorageService } from './storage/storage.service';
import { LoginGuard } from './guard/login.guard';
import { SessionUserService } from './session/session-user.service';
import { SecurityInterceptor } from './interceptor/security.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormHelperService } from './form/form-helper.service';
import { ErrorInterceptor } from './interceptor/error.interceptor';
import { InterceptorHelperService } from './interceptor/interceptor-helper.service';

@NgModule({
  declarations: [],
})
export class GmsCoreModule {

  /**
   * Module's constructor.
   * @param {GmsCoreModule} parentModule Self GmsCoreModule injected into its own constructor in order to guard against a lazy-loaded module
   * re-importing this module.
   */
  constructor(@Optional() @SkipSelf() parentModule: GmsCoreModule) {
    if (parentModule) {
      console.warn('GmsCoreModule is already loaded. Consider import it in the AppModule only if you are only' +
        'using its providers');
    }
  }

  /**
   * Method for getting a module's instance with providers.
   * @param config Optional configuration object with custom providers to be used. The following attributes can be set
   * in the object in order to set custom providers classes:
   * <p>- `sessionService` for providing a class instead of the default SessionService</p>
   * <p>- `storageService` for providing a class instead of the default StorageService</p>
   * <p>- `loginGuard` for providing a class instead of the default LoginGuard</p>
   * <p>- `sessionUserService` for providing a class instead of the default SessionUserService</p>
   * <p>Example of configuration object: { sessionService: MySessionService } </p>
   */
  static forRoot(config?: any): ModuleWithProviders {
    return {
      ngModule: GmsCoreModule,
      providers: [
        {
          provide: InterceptorHelperService,
          useClass: InterceptorHelperService
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: SecurityInterceptor,
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
        }
      ]
    };
  }
}
