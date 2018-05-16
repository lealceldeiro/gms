import { inject, TestBed } from '@angular/core/testing';

import { LoginGuard } from './login.guard';
import { SessionService } from '../session/session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable, of } from 'rxjs/index';

describe('LoginGuard', () => {

  const sessionServiceStub = { isNotLoggedIn: function (): Observable<boolean> { return of(false); } };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ LoginGuard, HttpClientTestingModule, { provide: SessionService, useValue: sessionServiceStub } ]
    });
  });

  it('should be created', inject([LoginGuard], (service: LoginGuard) => {
    expect(service).toBeTruthy();
  }));

  it('canActivateChild should return `false` if the user is logged in', () => {
    const guard = TestBed.get(LoginGuard);
    guard.canActivateChild().subscribe(activate => {
      expect(activate).toBeFalsy();
    });
  });

  it('canActivateChild should return `true` if the user is not logged in', () => {
    sessionServiceStub.isNotLoggedIn = function (): Observable<boolean> { return of(true); };
    const guard = TestBed.get(LoginGuard);
    guard.canActivateChild().subscribe(activate => {
      expect(activate).toBeTruthy();
    });
  });
});
