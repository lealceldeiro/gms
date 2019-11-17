import { Injectable } from '@angular/core';
import { CanActivateChild, CanLoad, Router } from '@angular/router';

import { Observable } from 'rxjs';
import { first, tap } from 'rxjs/operators';

import { SessionService } from '../session/session.service';

/**
 * Guards which determines whether the `login` route should be loaded and activated or not.
 */
@Injectable()
export class LoginGuard implements CanActivateChild, CanLoad {
  /**
   * Guards constructor.
   */
  constructor(private sessionService: SessionService, private router: Router) { }

  /**
   * Return an observable with  `true` if the login route can be activated, `false` otherwise.
   * @returns {Observable<boolean>}
   */
  canActivateChild(): Observable<boolean> {
    return this.getIsUserLoggedInAndGoHomeIfNot();
  }

  /**
   * Returns an observable with `true` if the login component can be loaded, `false` otherwise.
   * @returns {Observable<boolean>}
   */
  canLoad(): Observable<boolean> {
    return this.getIsUserLoggedInAndGoHomeIfNot();
  }

  /**
   * Navigates to the home page whatever it is.
   */
  private goToHome() {
    this.router.navigateByUrl('/home');
  }

  /**
   * Returns an observable with `true` if the user is logged in, `false` otherwise.
   * It navigates to the home page if the user is already logged in.
   * @returns {Observable<boolean>}
   */
  private getIsUserLoggedInAndGoHomeIfNot(): Observable<boolean> {
    return this.sessionService.isNotLoggedIn().pipe(tap((notLogged) => {
      if (!notLogged) { // logged?
        this.goToHome();
      }
    }), first());
  }
}
