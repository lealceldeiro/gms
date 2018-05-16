import { Injectable } from '@angular/core';
import { CanActivateChild, CanLoad } from '@angular/router';
import { SessionService } from '../session/session.service';

/**
 * Guards which determines whether the `login` route should be activated or not.
 */
@Injectable()
export class LoginGuard implements CanActivateChild, CanLoad {

  /**
   * Guards constructor.
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Return `true` if the login route can be activated, `false` otherwise.
   * @returns {boolean}
   */
  canActivateChild(): boolean {
    return !this.sessionService.isLoggedIn();
  }

  /**
   * Returns `true` if the login component can be loaded, `false` otherwise.
   * @returns {boolean}
   */
  canLoad(): boolean {
    return !this.sessionService.isLoggedIn();
  }

}
