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
    loggedIn: 'xSkd838DJdkfLo0P4J8K2K1J7juvnf38mcjdk',
    /**
     * Key under which the value security parameters such as `access_token` and `refresh_token` are stored in the StorageService.
     * @type {string}
     */
    loginData: 'mj8ddDj83DJ02kdkjDJ0_kdj9Dks9dkD23jCdkj7dfjDJ0Ck',
    /**
     * Key under which the session user's data is stored in the StorageService.
     * @type {string}
     */
    user: 'Sdkcndoi8jasdlu201039Jdjd92jAaw02kdDJ93jfLak93jh71d93dDJ7472Ak',
    /**
     * Key under which the access token is stored.
     */
    accessToken: 'Jsj89572ANjfnAkdnKSJDn837fNdu1048NDJSF83r4f7ndLDJ'
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
    this.store(this.key.loggedIn, value, false);
    this.loggedIn = value;
  }

  /**
   * Returns the current session user's info.
   * @returns {User}
   */
  getUser(): User {
    return this.user != null ? this.user : this.retrieve(this.key.user, false);
  }

  /**
   * Sets the current session user's info.
   * @param {User} value
   */
  setUser(value: User) {
    this.store(this.key.user, value, false);
    this.user = value;
  }

  /**
   * Returns the information provided by the authentication endpoint when logging in.
   * @returns {LoginResponseModel}
   */
  getAuthData(): LoginResponseModel {
    return this.authData != null ? this.authData : this.retrieve(this.key.loginData, false);
  }

  /**
   * Sets the information provided by the authentication endpoint when logging in
   * @param {LoginResponseModel | any} value
   */
  setAuthData(value: LoginResponseModel | any) {
    this.store(this.key.loginData, value as LoginResponseModel, false);
    this.store(this.key.accessToken, value.access_token); // shortcut for access token
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

  // region shortcuts
  /**
   * Returns the access token required for doing all secured requests to the API endpoints, provided by the authentication endpoint when
   * logging in.
   * @returns {string}
   */
  getAccessToken(): String {
    let token = this.retrieve(this.key.accessToken);
    if (!token) {
      token = this.getAuthData().access_token;
      if (token) {
        this.store(this.key.accessToken, token);
      }
    }
    return token;
  }

  /**
   * Returns the refresh token required for requesting a new access token without performing authentication again.
   * logging in.
   * @returns {String}
   */
  getRefreshToken(): String {
    return this.getAuthData().refresh_token;
  }

  /**
   * Returns the header to be sent with the access token.
   * @returns {String}
   */
  getHeader(): String {
    return this.getAuthData().header_to_be_sent;
  }

  /**
   * Returns the token type to be send in the header along with the access token.
   * @returns {String}
   */
  getTokenType(): String {
    return this.getAuthData().token_type;
  }
  // endregion

  // region private

  /**
   * Stores a value under a key using the StorageService dependency.
   * @param {string} key Key under which the value will be saved.
   * @param {boolean} inCookie Whether the value should be stored as a cookie or in the localStorage
   * @param {object} options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <pre><code>{expires: <value> {string|Date}</code></pre>
   * @param value Value to be stored.
   */
  private store(key: string, value: any, inCookie = true, options?: object) {
    inCookie ? this.storageService.putCookie(key, value, options) : this.storageService.set(key, value);
  }

  /**
   * Retrieves a value stored using the StorageService under a specific key.
   * @param {string} key Key under which the value was stored.
   * @param {boolean} fromCookie Whether the value is trying to be retrieved was stored in a cookie or not (in the localStorage)
   * @param {boolean} isObject Whether the value is trying to be retrieved is an object or not when retrieving value from cookie.
   * @returns {any}
   */
  private retrieve(key: string, fromCookie = true, isObject = false): any {
    return fromCookie ? this.storageService.getCookie(key, isObject) : this.storageService.get(key);
  }

  // endregion
}
