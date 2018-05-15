import { Injectable } from '@angular/core';
import { User } from './user.model';
import { LoginResponseModel } from './login-response.model';
import { StorageService } from '../storage/storage.service';

/**
 * A service for providing information about the current session.
 */
@Injectable()
export class SessionService {

  /**
   * Keys for storing information
   * @type object
   */
  private key = {
    /**
     * Key under which the value `loggedIn` is stored in the StorageService.
     * @type {string}
     */
    loggedIn: 'loggedIn',
    /**
     * Key under which the value security parameters such as `access_token` and `refresh_token` are stored in the StorageService.
     * @type {string}
     */
    loginData: 'loginData',
    /**
     * Key under which the session user's data is stored in the StorageService.
     * @type {string}
     */
    user: 'user'
  };

  /**
   * Whether the user is logged in or not.
   * @type {boolean}
   */
  private loggedIn: boolean;

  /**
   * Session user's info (if available).
   */
  private user?: User;

  /**
   * Session auth's info (if available).
   */
  private authData?: LoginResponseModel;

  /**
   * Indicates whether the session info should be kept after the user left the app without login out or not.
   * @type {boolean}
   */
  private rememberMe = false;

  /**
   * Service constructor.
   * @param storageService StorageService for storing session-related information.
   */
  constructor(private storageService: StorageService) { }

  /**
   * Returns whether the user is logged in or not
   * @returns {boolean}
   */
  isLoggedIn(): boolean {
    return this.loggedIn != null ? this.loggedIn : this.retrieve(this.key.loggedIn);
  }

  /**
   * Sets whther the user is loggued in or not
   * @param {boolean} value
   */
  setLoggedIn(value: boolean) {
    this.store(this.key.loggedIn, value);
    this.loggedIn = value;
  }

  /**
   * Returns the current session user's info.
   * @returns {User}
   */
  getUser(): User {
    return this.user != null ? this.user : this.retrieve(this.key.user);
  }

  /**
   * Sets the current session user's info.
   * @param {User} value
   */
  setUser(value: User) {
    this.store(this.key.user, value);
    this.user = value;
  }

  /**
   * Returns the information provided by the authentication endpoint when logging in.
   * @returns {LoginResponseModel}
   */
  getAuthData(): LoginResponseModel {
    return this.authData != null ? this.authData : this.retrieve(this.key.loginData);
  }

  /**
   * Sets the information provided by the authentication endpoint when logging in
   * @param {LoginResponseModel | any} value
   */
  setAuthData(value: LoginResponseModel | any) {
    this.store(this.key.loginData, value as LoginResponseModel);
    this.authData = value;
  }

  /**
   * Returns whether the credentials should be stored or not when logging in.
   * @returns {boolean}
   */
  isRememberMe(): boolean {
    return this.rememberMe;
  }

  /**
   * Sets whether the credentials should be stored or not when logging in.
   * @param {boolean} value
   */
  setRememberMe(value: boolean) {
    this.rememberMe = value;
  }

  /**
   * Stores a value under a key using the StorageService dependency.
   * @param {string} key Key under which the value will be saved.
   * @param value Value to be stored.
   */
  private store(key: string, value: any) {
    this.storageService.set(key, value);
  }

  /**
   * Retrieves a value stored using the StorageService under a specific key.
   * @param {string} key Key under which the value was stored.
   * @returns {any}
   */
  private retrieve(key: string): any {
    return this.storageService.get(key);
  }
}
