import { HttpClientTestingModule } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs/index';
import { first } from 'rxjs/operators';
import { SessionService } from '../session/session.service';
import { LoginGuard } from './login.guard';

describe('LoginGuard', () => {
  let guard: LoginGuard;
  const isNotLoggedInSb = new BehaviorSubject<boolean>(false);
  let navigatedByUrlSpy: jasmine.Spy;

  const sessionServiceStub = { isNotLoggedIn: () => isNotLoggedInSb.asObservable() };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [LoginGuard, HttpClientTestingModule, { provide: SessionService, useValue: sessionServiceStub }]
    });
    guard = TestBed.get(LoginGuard);
    navigatedByUrlSpy = spyOn((<any>guard).router, 'navigateByUrl');
  });

  it('should be created', inject([LoginGuard], (service: LoginGuard) => {
    expect(service).toBeTruthy();
  }));

  it('canActivateChild should return `false` if the user is logged in', () => {
    isNotLoggedInSb.next(false);
    guard.canActivateChild().pipe(first()).subscribe((activate: boolean) => {
      expect<boolean>(activate).toBeFalsy();
      expect(navigatedByUrlSpy).toHaveBeenCalledTimes(1);
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
      expect(navigatedByUrlSpy).toHaveBeenCalledTimes(1);
    });
  });

  it('canLoad should return `true` if the user is not logged in', () => {
    isNotLoggedInSb.next(true);
    guard.canLoad().pipe(first()).subscribe((load: boolean) => {
      expect<boolean>(load).toBeTruthy();
    });
  });
});
