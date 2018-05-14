import { Injectable } from '@angular/core';
import { CanActivateChild } from '@angular/router';
import { SessionService } from '../session/session.service';

/**
 * Guards which determines whether the `login` route should be activated or not.
 */
@Injectable()
export class LoginGuard implements CanActivateChild {

  /**
   * Guards constructor.
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Return `true` if the login route can be activated, `false` otherwise.
   * @returns {boolean}
   */
  canActivateChild() {
    return !this.sessionService.loggedIn;
  }
}
