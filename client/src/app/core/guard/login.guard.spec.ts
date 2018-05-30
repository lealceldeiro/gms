import { inject, TestBed } from '@angular/core/testing';

import { LoginGuard } from './login.guard';
import { SessionService } from '../session/session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BehaviorSubject } from 'rxjs/index';
import { first } from 'rxjs/internal/operators';

describe('LoginGuard', () => {
  let guard: LoginGuard;
  const isNotLoggedInSb = new BehaviorSubject<boolean>(false);

  const sessionServiceStub = { isNotLoggedIn: () => isNotLoggedInSb.asObservable() };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ LoginGuard, HttpClientTestingModule, { provide: SessionService, useValue: sessionServiceStub } ]
    });
    guard = TestBed.get(LoginGuard);
  });

  it('should be created', inject([LoginGuard], (service: LoginGuard) => {
    expect(service).toBeTruthy();
  }));

  it('canActivateChild should return `false` if the user is logged in', () => {
    isNotLoggedInSb.next(false);
    guard.canActivateChild().pipe(first()).subscribe((activate: boolean) => {
      expect<boolean>(activate).toBeFalsy();
    });
  });

  it('canActivateChild should return `true` if the user is not logged in', () => {
    isNotLoggedInSb.next(true);
    guard.canActivateChild().pipe(first()).subscribe((activate: boolean) => {
      expect<boolean>(activate).toBeTruthy();
    });
  });

  it('canLoad should return `false` if the user is logged in', () => {
    isNotLoggedInSb.next(false);
    guard.canLoad().pipe(first()).subscribe((load: boolean) => {
      expect<boolean>(load).toBeFalsy();
    });
  });

  it('canLoad should return `true` if the user is not logged in', () => {
    isNotLoggedInSb.next(true);
    guard.canLoad().pipe(first()).subscribe((load: boolean) => {
      expect<boolean>(load).toBeTruthy();
    });
  });
});
