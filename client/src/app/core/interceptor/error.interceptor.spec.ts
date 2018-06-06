import { inject, TestBed } from '@angular/core/testing';

import { ErrorInterceptor } from './error.interceptor';
import { ToastrService } from 'ngx-toastr';
import { InterceptorHelperService } from './interceptor-helper.service';

describe('ErrorInterceptor', () => {
  const intHelperServiceStub = { isExcludedFromErrorHandling: () => {} };
  const toastrStub = {};

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ErrorInterceptor, { provide: ToastrService, useValue: toastrStub },
        { provide: InterceptorHelperService, useValue: intHelperServiceStub }]
    });
  });

  it('should be created', inject([ErrorInterceptor], (service: ErrorInterceptor) => {
    expect(service).toBeTruthy();
  }));
});
