import { TestBed } from '@angular/core/testing';

import { AllRequestsInterceptor } from './all-requests.interceptor.service';

describe('AllRequests.InterceptorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AllRequestsInterceptor = TestBed.get(AllRequestsInterceptor);
    expect(service).toBeTruthy();
  });
});
