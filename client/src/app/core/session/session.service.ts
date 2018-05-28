import { Injectable } from '@angular/core';
import { User } from './user.model';
import { LoginResponseModel } from './login-response.model';
import { StorageService } from '../storage/storage.service';
import { BehaviorSubject, Observable } from 'rxjs/index';
import { tap } from 'rxjs/internal/operators';

/**
 * A service for providing information about the current session.
 */
@Injectable()
export class SessionService {

  // region properties
  /**
   * Keys for storing information
   * @type object
   */
  private key = {
    /**
     * Key under which the value `loggedIn` is stored in the StorageService.
     * @type {string}
     */
    loggedIn: 'lIxSkd838DJdkfLo0P4J8K2K1J7juvnf38mcjdk',
    /**
     * Key under which the value `notLoggedIn` is stored in the StorageService.
     * @type {string}
     */
    notLoggedIn: 'nLif8H7D0f6DmajD727Dj39fMf74jfKFje729Djj29faLSk38sdJD237jd210F',
    /**
     * Key under which the value security parameters such as `access_token` and `refresh_token` are stored in the StorageService.
     * @type {string}
     */
    loginData: 'lDmj8ddDj83DJ02kdkjDJ0_kdj9Dks9dkD23jCdkj7dfjDJ0Ck',
    /**
     * Key under which the session user's data is stored in the StorageService.
     * @type {string}
     */
    user: 'uSdkcndoi8jasdlu201039Jdjd92jAaw02kdDJ93jfLak93jh71d93dDJ7472Ak',
    /**
     * Key under which the access token is stored.
     */
    accessToken: 'aTJsj89572ANjfnAkdnKSJDn837fNdu1048NDJSF83r4f7ndLDJ',
    /**
     * Key under which the refresh token is stored.
     */
    refreshToken: 'rTjdAjs9239DSJdj5720DSJKfn8D8F0F3D9f03kvmg73Djf830sfj',
    /**
     * Key under which the header to be sent carrying the access token is stored.
     */
    headerToBeSent: 'hTbSjfUSdjdMfnfu8329DHsn82Dj9fD0D89D76D7888Dj6DnNMg76s7a82j4N47F',
    /**
     * Key under which the token type is stored.
     */
    tokenType: 'tTn7ND92Md7dHd88dh62Dn6DJj3Djj3jS9Kd8di8D8shh3msd'
  };

  /**
   * Whether the user is logged in or not (`true` indicates the user is logged in, `false` otherwise).
   * @type {boolean}
   */
  private loggedIn = new BehaviorSubject<boolean>(false);

  /**
   * Whether the user is NOT logged he/she is (`true` indicates the user is NOT logged in, `false` otherwise).
   * @type {boolean}
   */
  private notLoggedIn = new BehaviorSubject<boolean>(true);

  /**
   * Observable holding whether the user is logged in or not (`true` indicates the user is logged in, `false` otherwise).
   * @type {Observable<boolean>}
   */
  private loggedIn$ = this.loggedIn.asObservable();

  /**
   * Observable holding whether the user is NOT logged he/she is (`true` indicates the user is NOT logged in, `false` otherwise).
   * @type {Observable<boolean>}
   */
  private notLoggedIn$ = this.notLoggedIn.asObservable();

  /**
   * Session user's info (if available).
   * @type {BehaviorSubject<User>}
   */
  private user = new BehaviorSubject<User>(null);

  /**
   * Observable holding the session user's info (if available).
   * @type {Observable<User>}
   */
  private user$ = this.user.asObservable();

  /**
   * Session auth's info (if available).
   */
  private authData = new BehaviorSubject<LoginResponseModel>({});

  /**
   * Observable holding the session auth's info (if available).
   * @type {Observable<LoginResponseModel>}
   */
  private authData$ = this.authData.asObservable();

  /**
   * Indicates whether the session info should be kept after the user left the app without login out or not.
   * @type {boolean}
   */
  private rememberMe = false;
  // endregion

  /**
   * Service constructor.
   * @param storageService StorageService for storing session-related information.
   */
  constructor(private storageService: StorageService) {
    this.loadInitialData();
  }

  /**
   * Indicates whether the user is logged in.
   * @returns {Observable<boolean>} An observable containing `true` if the user is logged in, `false` otherwise.
   * @see isNotLoggedIn
   */
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$.pipe(tap(() => {}, (e) => { console.warn(e); }));
  }

  /**
   * Indicates whether the user is not logged in.
   * @returns {Observable<boolean>} An observable containing `true` if the user is not logged in, `false` otherwise.
   * @see isLoggedIn
   */
  isNotLoggedIn(): Observable<boolean> {
    return this.notLoggedIn$.pipe(tap(() => {}, (e) => { console.warn(e); }));
  }

  /**
   * Sets whether the user is logged in or not.
   * @param {boolean} value
   */
  setLoggedIn(value: boolean) {
    this.store(this.key.loggedIn, value, false);
    this.store(this.key.notLoggedIn, !value, false);
    this.loggedIn.next(value);
    this.notLoggedIn.next(!value);
  }

  /**
   * Returns the current session user's info.
   * @returns {Observable<User>}
   */
  getUser(): Observable<User> {
    return this.user$;
  }

  /**
   * Sets the current session user's info.
   * @param {User} value
   */
  setUser(value: User) {
    this.store(this.key.user, value, false);
    this.user.next(value);
  }

  /**
   * Returns the information provided by the authentication endpoint when logging in.
   * @returns {Observable<LoginResponseModel>}
   */
  getAuthData(): Observable<LoginResponseModel> {
    return this.authData$;
  }

  /**
   * Sets the information provided by the authentication endpoint when logging in
   * @param {LoginResponseModel | any} value
   */
  setAuthData(value: LoginResponseModel | any) {
    this.store(this.key.loginData, value as LoginResponseModel, false);
    this.store(this.key.accessToken, value.access_token);                         // shortcut for access token
    this.store(this.key.refreshToken, value.refresh_token);                       // shortcut for refresh token
    this.store(this.key.headerToBeSent, value.header_to_be_sent);                 // shortcut for header to be sent
    this.store(this.key.tokenType, value.token_type);                             // shortcut for token type
    this.authData.next(value);
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
   * Deletes all session-related user information
   */
  closeSession(): void {
    this.loggedIn.next(false);
    this.notLoggedIn.next(true);
    this.authData.next({});
    this.user.next(null);

    const lis = this.storageService.clear(this.key.loggedIn).subscribe(() => {
      if (lis) { // check for testing purposes
        lis.unsubscribe();
      }
    });
    const nlis = this.storageService.clear(this.key.notLoggedIn).subscribe(() => {
      if (nlis) { // check for testing purposes
        nlis.unsubscribe();
      }
    });
    const lds = this.storageService.clear(this.key.loginData).subscribe(() => {
      if (lds) { // check for testing purposes
        lds.unsubscribe();
      }
    });
    const ats = this.storageService.clearCookie(this.key.accessToken).subscribe(() => {
      if (ats) { // check for testing purposes
        ats.unsubscribe();
      }
    });
    const rts = this.storageService.clearCookie(this.key.refreshToken).subscribe(() => {
      if (rts) { // check for testing purposes
        rts.unsubscribe();
      }
    });
    const htbss = this.storageService.clearCookie(this.key.headerToBeSent).subscribe(() => {
      if (htbss) { // check for testing purposes
        htbss.unsubscribe();
      }
    });
    const tts = this.storageService.clearCookie(this.key.tokenType).subscribe(() => {
      if (tts) { // check for testing purposes
        tts.unsubscribe();
      }
    });
    const us = this.storageService.clear(this.key.user).subscribe(() => {
      if (us) { // check for testing purposes
        us.unsubscribe();
      }
    });
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
        const o$ = this.getAuthData().subscribe((data: LoginResponseModel) => {
          if (data && data.access_token) {
            this.store(this.key.accessToken, data.access_token);
            if (o$) {
              o$.unsubscribe();
            }
          }
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
        const o$ = this.getAuthData().subscribe((data: LoginResponseModel) => {
          if (data && data.refresh_token) {
            this.store(this.key.refreshToken, data.refresh_token);
            if (o$) {
              o$.unsubscribe();
            }
          }
        });
      }
    }));
  }

  /**
   * Returns the header to be sent with the access token.
   * @returns {Observable<String>}
   */
  getHeader(): Observable<String> {
    return this.retrieve(this.key.headerToBeSent).pipe(tap((headerTbS) => {
      if (!headerTbS) {
        const o$ = this.getAuthData().subscribe((data: LoginResponseModel) => {
          if (data && data.header_to_be_sent) {
            this.store(this.key.headerToBeSent, data.header_to_be_sent);
            if (o$) {
              o$.unsubscribe();
            }
          }
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
        const o$ = this.getAuthData().subscribe((data: LoginResponseModel) => {
          if (data && data.token_type) {
            this.store(this.key.tokenType, data.token_type);
            if (o$) {
              o$.unsubscribe();
            }
          }
        });
      }
    }));
  }
  // endregion

  // region private

  /**
   * Load some initial data this service manages and gets from storage service.
   */
  private loadInitialData() {
    // load data
    const oli = this.retrieve(this.key.loggedIn, false).subscribe((val) => {
      this.loggedIn.next(val === true);
      if (oli) { // check for testing purposes
        oli.unsubscribe();
      }
    });
    const onli = this.retrieve(this.key.notLoggedIn, false).subscribe((val) => {
      this.notLoggedIn.next(val === true || val === null);
      if (onli) { // check for testing purposes
        onli.unsubscribe();
      }
    });
    const ou = this.retrieve(this.key.user, false).subscribe((val) => {
      this.user.next(val);
      if (ou) { // check for testing purposes
        ou.unsubscribe();
      }
    });
    const oad = this.retrieve(this.key.loginData, false).subscribe((val) => {
      this.authData.next(val ? val : {});
      if (oad) { // check for testing purposes
        oad.unsubscribe();
      }
    });
  }

  /**
   * Stores a value under a key using the StorageService dependency.
   * @param {string} key Key under which the value will be saved.
   * @param {boolean} inCookie Whether the value should be stored as a cookie or in the localStorage
   * @param {object} options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <gmsCk><code>{expires: <value> {string|Date}</code></gmsCk>
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
