import { Injectable } from '@angular/core';
import { CanActivateChild, CanLoad } from '@angular/router';
import { SessionService } from '../session/session.service';
import { Observable } from 'rxjs/index';
import { first } from 'rxjs/internal/operators';

/**
 * Guards which determines whether the `login` route should be loadded and activated or not.
 */
@Injectable()
export class LoginGuard implements CanActivateChild, CanLoad {

  /**
   * Guards constructor.
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Return an observable with  `true` if the login route can be activated, `false` otherwise.
   * @returns {Observable<boolean>}
   */
  canActivateChild(): Observable<boolean> {
    return this.sessionService.isNotLoggedIn().pipe(first());
  }

  /**
   * Returns an observable with `true` if the login component can be loaded, `false` otherwise.
   * @returns {Observable<boolean>}
   */
  canLoad(): Observable<boolean> {
    return this.sessionService.isNotLoggedIn().pipe(first());
  }
}
