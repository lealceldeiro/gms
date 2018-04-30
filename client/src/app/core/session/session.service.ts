import { Injectable } from '@angular/core';
import { User } from './user.model';

/**
 * A service for providing information about the current session.
 */
@Injectable()
export class SessionService {

  /**
   * Whether the user is logged in or not.
   * @type {boolean}
   */
  public loggedIn = false;

  /**
   * Session user's info (if available).
   */
  public user?: User;

  /**
   * Service constructor.
   */
  constructor() { }

}
