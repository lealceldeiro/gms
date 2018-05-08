import { Injectable } from '@angular/core';
import { UserStub } from './user-stub.model';

/**
 * A stub service for helping in testing the component which depend upon the SessionService.
 */
@Injectable()
export class SessionStubService {

  /**
   * Whether the user is logged in or not.
   * @type {boolean}
   */
  public loggedIn = false;

  /**
   * Session user's info (if available).
   */
  public user?: UserStub;

  /**
   * Indicates whether the session info should be kept after the user left the app without login out or not.
   * @type {boolean}
   */
  public rememberMe = false;

  /**
   * Service constructor.
   */
  constructor() { }

}
