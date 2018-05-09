import { inject, TestBed } from '@angular/core/testing';

import { LoginGuard } from './login.guard';
import { SessionService } from '../session/session.service';

describe('LoginGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoginGuard, { provide: SessionService, useValue: {}}]
    });
  });

  it('should be created', inject([LoginGuard], (service: LoginGuard) => {
    expect(service).toBeTruthy();
  }));
});
