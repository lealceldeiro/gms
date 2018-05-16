import { Injectable } from '@angular/core';
import { User } from './user.model';
import { LoginResponseModel } from './login-response.model';
import { StorageService } from '../storage/storage.service';
import { Observable, of } from 'rxjs/index';
import { tap } from 'rxjs/internal/operators';

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
     * Key under which the value `notLoggedIn` is stored in the StorageService.
     * @type {string}
     */
    notLoggedIn: 'f8H7D0f6DmajD727Dj39fMf74jfKFje729Djj29faLSk38sdJD237jd210F',
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
    accessToken: 'Jsj89572ANjfnAkdnKSJDn837fNdu1048NDJSF83r4f7ndLDJ',
    /**
     * Key under which the refresh token is stored.
     */
    refreshToken: 'jdAjs9239DSJdj5720DSJKfn8D8F0F3D9f03kvmg73Djf830sfj',
    /**
     * Key under which the header to be sent carrying the access token is stored.
     */
    headerToBeSent: 'jfUSdjdMfnfu8329DHsn82Dj9fD0D89D76D7888Dj6DnNMg76s7a82j4N47F',
    /**
     * Key under which the token type is stored.
     */
    tokenType: 'n7ND92Md7dHd88dh62Dn6DJj3Djj3jS9Kd8di8D8shh3msd'
  };

  /**
   * Whether the user is logged in or not (`true` indicates the user is logged in, `false` otherwise).
   * @type {boolean}
   */
  private loggedIn = false;

  /**
   * Whether the user is NOT logged he/she is (`true` indicates the user is NOT logged in, `false` otherwise).
   * @type {boolean}
   */
  private notLoggedIn = true;

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
   * Indicates whether the user is logged in.
   * @returns {Observable<boolean>} An observable containing `true` if the user is logged in, `false` otherwise.
   * @see isNotLoggedIn
   */
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn != null ? of(this.loggedIn)
      : (this.notLoggedIn != null ? of(!this.notLoggedIn) : this.retrieve(this.key.loggedIn, false));
  }

  /**
   * Indicates whether the user is logged in.
   * @returns {Observable<boolean>} An observable containing `true` if the user is not logged in, `false` otherwise.
   * @see isLoggedIn
   */
  isNotLoggedIn(): Observable<boolean> {
    return this.notLoggedIn != null ? of(this.notLoggedIn)
      : (this.loggedIn != null ? of(!this.loggedIn) : this.retrieve(this.key.notLoggedIn, false));
  }

  /**
   * Sets whther the user is loggued in or not
   * @param {boolean} value
   */
  setLoggedIn(value: boolean) {
    this.store(this.key.loggedIn, value, false);
    this.store(this.key.notLoggedIn, !value, false);
    this.loggedIn = value;
    this.loggedIn = !value;
  }

  /**
   * Returns the current session user's info.
   * @returns {Observable<User>}
   */
  getUser(): Observable<User> {
    return this.user != null ? of(this.user) : this.retrieve(this.key.user, false);
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
   * @returns {Observable<LoginResponseModel>}
   */
  getAuthData(): Observable<LoginResponseModel> {
    return this.authData != null ? of(this.authData) : this.retrieve(this.key.loginData, false);
  }

  /**
   * Sets the information provided by the authentication endpoint when logging in
   * @param {LoginResponseModel | any} value
   */
  setAuthData(value: LoginResponseModel | any) {
    this.store(this.key.loginData, value as LoginResponseModel, false);
    this.store(this.key.accessToken, value.access_token); // shortcut for access token
    this.store(this.key.refreshToken, value.refresh_token); // shortcut for refresh token
    this.store(this.key.headerToBeSent, value.header_to_be_sent); // shortcut for refresh token
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
   * @returns {Observable<string>}
   */
  getAccessToken(): Observable<String> {
    return this.retrieve(this.key.accessToken).pipe(tap((aToken) => {
      if (!aToken) {
        this.getAuthData().subscribe((authToken) => {
          this.store(this.key.accessToken, authToken);
        });
      }
    }));
  }

  /**
   * Returns the refresh token required for requesting a new access token without performing authentication again.
   * logging in.
   * @returns {Observable<String>}
   */
  getRefreshToken(): Observable<String> {
    return this.retrieve(this.key.refreshToken).pipe(tap((rToken) => {
      if (!rToken) {
        this.getAuthData().subscribe((refreshToken) => {
          this.store(this.key.refreshToken, refreshToken);
        });
      }
    }));
  }

  /**
   * Returns the header to be sent with the access token.
   * @returns {Observable<String>}
   */
  getHeader(): Observable<String> {
    return this.retrieve(this.key.refreshToken).pipe(tap((rToken) => {
      if (!rToken) {
        this.getAuthData().subscribe((refreshToken) => {
          this.store(this.key.refreshToken, refreshToken);
        });
      }
    }));
  }

  /**
   * Returns the token type to be send in the header along with the access token.
   * @returns {Observable<String>}
   */
  getTokenType(): Observable<String> {
    return this.retrieve(this.key.tokenType).pipe(tap((tType) => {
      if (!tType) {
        this.getAuthData().subscribe((tokenType) => {
          this.store(this.key.tokenType, tokenType);
        });
      }
    }));
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
   * @returns {Observable<any>}
   */
  private retrieve(key: string, fromCookie = true, isObject = false): Observable<any> {
    return fromCookie ? this.storageService.getCookie(key, isObject) : this.storageService.get(key);
  }

  // endregion
}
