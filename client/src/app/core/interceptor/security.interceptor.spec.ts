import { inject, TestBed } from '@angular/core/testing';

import { SecurityInterceptor } from './security.interceptor';
import { SessionService } from '../session/session.service';

describe('SecurityInterceptor', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SecurityInterceptor, { provide: SessionService, useValue: {}}]
    });
  });

  it('should be created', inject([SecurityInterceptor], (service: SecurityInterceptor) => {
    expect(service).toBeTruthy();
  }));
});
