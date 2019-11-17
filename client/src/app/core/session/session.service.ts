import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';
import * as _ from 'lodash';

import { StorageService } from '../storage/storage.service';
import { LoginResponseModel } from './login-response.model';
import { User } from './user.model';
import { AppConfig } from '../config/app.config';
import { ISecurityKey } from '../model/config/app-config.model';
import { Util } from '../util/util';
import { TruthyPredicate } from '../predicate/truthy.predicate';

/**
 * A service for providing information about the current session.
 * This service provide a method `loadInitialData` which must be called on app start up.
 *
 * @see loadInitialData
 */
@Injectable()
export class SessionService {
  // region properties
  /**
   * Whether the user is logged in or not (`true` indicates the user is logged in, `false` otherwise).
   */
  private loggedIn = new BehaviorSubject<boolean>(false);

  /**
   * Observable holding `this.loggedIn`.
   */
  private loggedIn$ = this.loggedIn.asObservable();

  /**
   * Whether the user is NOT logged he/she is (`true` indicates the user is NOT logged in, `false` otherwise).
   */
  private notLoggedIn = new BehaviorSubject<boolean>(true);

  /**
   * Observable holding `this.notLoggedIn`.
   */
  private notLoggedIn$ = this.notLoggedIn.asObservable();

  /**
   * Session user's info (if available).
   */
  private user = new BehaviorSubject<User>(new User());

  /**
   * Observable holding `this.user`.
   */
  private user$ = this.user.asObservable();

  /**
   * Session auth's info (if available).
   */
  private authData = new BehaviorSubject<LoginResponseModel>({});

  /**
   * Observable holding `this.authData`.
   */
  private authData$ = this.authData.asObservable();

  /**
   * Indicates whether the session info should be kept after the user left the app without login out or not.
   */
  private rememberMe = new BehaviorSubject<boolean>(false);

  /**
   * Observable holding `this.rememberMe`.
   */
  private rememberMe$ = this.rememberMe.asObservable();

  /**
   * Access token to be sent along in the API request headers.
   */
  private accessToken = new BehaviorSubject<string>('');

  /**
   * Observable holding `this.accessToken`.
   */
  private accessToken$ = this.accessToken.asObservable();

  /**
   * Refresh token used to request new authentication credentials.
   */
  private refreshToken = new BehaviorSubject<string>('');

  /**
   * Observable holding `this.refreshToken`.
   */
  private refreshToken$ = this.refreshToken.asObservable();

  /**
   * Header to be sent with the access token.
   */
  private headerToBeSent = new BehaviorSubject<string>('');

  /**
   * Observable holding `this.headerToBeSent`.
   */
  private headerToBeSent$ = this.headerToBeSent.asObservable();

  /**
   * Token type to be sent in the header along with the access token.
   */
  private tokenType = new BehaviorSubject<string>('');

  /**
   * Observable holding `this.tokenType`.
   */
  private tokenType$ = this.tokenType.asObservable();
  // endregion

  /**
   * Service constructor.
   *
   * @param storageService StorageService for storing session-related information.
   */
  constructor(private storageService: StorageService) { }

  /**
   * Loads all the the data regarding the user session. This method must be called on app startup in order to load information such as
   * login status of the user.
   */
  loadInitialData(): void {
    // load data
    this.retrieve(this.loggedInKey).subscribe((val) => {
      this.loggedIn.next(val === true || val === 'true');
    });
    this.retrieve(this.notLoggedInKey).subscribe((val) => {
      this.notLoggedIn.next(val === true || val === 'true' || val === null || val === undefined);
    });
    this.retrieve(this.userKey, false).subscribe((val) => {
      this.user.next(val);
    });
    this.retrieve(this.loginDataKey, false).subscribe((val) => {
      this.authData.next(val || {});
    });
    this.retrieve(this.rememberMeKey, true).subscribe((r) => {
      this.rememberMe.next(r === true || r === 'true');
    });
  }

  /**
   * Indicates whether the user is logged in.
   *
   * @returns {Observable<boolean>} An observable containing `true` if the user is logged in, `false` otherwise.
   * @see isNotLoggedIn
   */
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$;
  }

  /**
   * Indicates whether the user is not logged in.
   *
   * @returns {Observable<boolean>} An observable containing `true` if the user is not logged in, `false` otherwise.
   * @see isLoggedIn
   */
  isNotLoggedIn(): Observable<boolean> {
    return this.notLoggedIn$;
  }

  /**
   * Sets whether the user is logged in or not.
   *
   * @param {boolean} value Value to be set.
   */
  setLoggedIn(value: boolean): void {
    if (!this.keysAreValid(this.loggedInKey, this.notLoggedInKey)) {
      return;
    }

    this.store(this.loggedInKey, value);
    this.store(this.notLoggedInKey, !value);
    this.loggedIn.next(value);
    this.notLoggedIn.next(!value);
  }

  /**
   * Returns the current session user's info.
   *
   * @returns {Observable<User>}
   */
  getUser(): Observable<User> {
    return this.user$;
  }

  /**
   * Sets the current session user's info.
   *
   * @param {User} value Value to be set.
   */
  setUser(value: User): void {
    if (!this.keysAreValid(this.userKey)) {
      return;
    }

    this.store(this.userKey, value, false);
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
   * Sets the information provided by the authentication endpoint when logging in.
   *
   * @param {LoginResponseModel | any} value Value to ve set.
   */
  setAuthData(value: LoginResponseModel | any): void {
    if (!this.keysAreValid(this.loginDataKey, this.accessTokenKey, this.refreshTokenKey, this.headerToBeSentKey, this.tokenTypeKey)) {
      return;
    }

    this.store(this.loginDataKey, value as LoginResponseModel, false);
    this.authData.next(value);
    this.setAccessToken(value.access_token);
    this.setRefreshToken(value.refresh_token);
    this.setHeader(value.header_to_be_sent);
    this.setTokenType(value.token_type);
  }

  /**
   * Returns whether the credentials should be stored or not when logging in.
   *
   * @returns {Observable<boolean>}
   */
  isRememberMe(): Observable<boolean> {
    return this.rememberMe$;
  }

  /**
   * Sets whether the credentials should be stored or not when logging in.
   *
   * @param {boolean} value Value to be set.
   */
  setRememberMe(value: boolean): void {
    if (!this.keysAreValid(this.rememberMeKey)) {
      return;
    }

    this.store(this.rememberMeKey, value, true);
    this.rememberMe.next(value);
  }

  /**
   * Returns the access token required for doing all secured requests to the API endpoints, provided by the authentication endpoint when
   * logging in.
   *
   * @returns {Observable<string>}
   */
  getAccessToken(): Observable<string> {
    return this.accessToken$;
  }

  /**
   * Sets the access token required for doing all secured requests to the API endpoints, provided by the authentication endpoint when
   * logging in.
   *
   * @param {string} value Value to be set.
   */
  setAccessToken(value: string): void {
    if (!this.keysAreValid(this.accessTokenKey)) {
      return;
    }

    this.store(this.accessTokenKey, value, true);
    this.accessToken.next(value);
  }

  /**
   * Returns the refresh token required for requesting a new access token without performing authentication again.
   * logging in.
   *
   * @returns {Observable<String>}
   */
  getRefreshToken(): Observable<string> {
    return this.refreshToken$;
  }

  /**
   * Sets the refresh token required for requesting a new access token without performing authentication again.
   * logging in.
   *
   * @param {string} value Value to be set.
   */
  setRefreshToken(value: string): void {
    if (!this.keysAreValid(this.refreshTokenKey)) {
      return;
    }

    this.store(this.refreshTokenKey, value, true);
    this.refreshToken.next(value);
  }

  /**
   * Returns the header to be sent with the access token.
   *
   * @returns {Observable<String>}
   */
  getHeader(): Observable<string> {
    return this.headerToBeSent$;
  }

  /**
   * Sets the header to be sent with the access token.
   *
   * @param {string} value Value to be set.
   */
  setHeader(value: string): void {
    if (!this.keysAreValid(this.headerToBeSentKey)) {
      return;
    }

    this.store(this.headerToBeSentKey, value, true);
    this.headerToBeSent.next(value);
  }

  /**
   * Returns the token type to be sent in the header along with the access token.
   *
   * @returns {Observable<String>}
   */
  getTokenType(): Observable<string> {
    return this.tokenType$;
  }

  /**
   * Sets the token type to be sent in the header along with the access token.
   */
  setTokenType(value: string): void {
    if (!this.keysAreValid(this.tokenTypeKey)) {
      return;
    }

    this.store(this.tokenTypeKey, value, true);
    this.tokenType.next(value);
  }

  /**
   * Deletes all session-related user information
   */
  closeSession(): void {
    if (!this.keysAreValid(
      this.loggedInKey,
      this.loginDataKey,
      this.accessTokenKey,
      this.refreshTokenKey,
      this.headerToBeSentKey,
      this.tokenTypeKey,
      this.userKey
    )) {
      return;
    }

    this.setLoggedIn(false);
    this.setAuthData({});
    this.setUser(new User());

    this.storageService.clearCookie(this.loggedInKey).subscribe(() => { /* no-op */ });
    this.storageService.clearCookie(this.notLoggedInKey).subscribe(() => { /* no-op */ });
    this.storageService.clear(this.loginDataKey).subscribe(() => { /* no-op */ });
    this.storageService.clearCookie(this.accessTokenKey).subscribe(() => { /* no-op */ });
    this.storageService.clearCookie(this.refreshTokenKey).subscribe(() => { /* no-op */ });
    this.storageService.clearCookie(this.headerToBeSentKey).subscribe(() => { /* no-op */ });
    this.storageService.clearCookie(this.tokenTypeKey).subscribe(() => { /* no-op */ });
    this.storageService.clear(this.userKey).subscribe(() => { /* no-op */ });
  }

  // region private

  /**
   * Stores a value under a key using the StorageService dependency.
   *
   * @param {string} key Key under which the value will be saved.
   * @param {boolean} inCookie Whether the value should be stored as a cookie or in the localStorage
   * @param {object} options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <gmsCk><code>{expires: <value> {string|Date}</code></gmsCk>
   * @param value Value to be stored.
   */
  private store(key: string, value: any, inCookie = true, options?: object): void {
    if (!key) {
      return;
    }

    inCookie ? this.storageService.putCookie(key, value, options) : this.storageService.set(key, value);
  }

  /**
   * Retrieves a value stored using the StorageService under a specific key.
   *
   * @param {string} key Key under which the value was stored.
   * @param {boolean} fromCookie Whether the value is trying to be retrieved was stored in a cookie or not (in the localStorage)
   * @param {boolean} isObject Whether the value is trying to be retrieved is an object or not when retrieving value from cookie.
   * @returns {Observable<any>}
   */
  private retrieve(key: string, fromCookie = true, isObject = false): Observable<any> {
    return fromCookie ? this.storageService.getCookie(key, isObject) : this.storageService.get(key);
  }

  /**
   * Gets the value of a key given it's name.
   *
   * @param name Name of the key to be retrieved.
   * @returns {string} the value of the key or `undefined` if the key is not present.
   * @see {AppConfig}
   */
  private _key(name: keyof ISecurityKey | string): string {
    return _.get(AppConfig.settings, `security.hash.key.${name}`);
  }

  /**
   * Checks that all keys are valid.
   *
   * @param keys Keys to be checked.
   * @returns {boolean} true if all the `keys` are valid, false otherwise.
   */
  private keysAreValid(...keys: (ISecurityKey | string)[]): boolean {
    return Util.allValuesFulfil(new TruthyPredicate<ISecurityKey | string>(), ...keys);
  }

  // region key getters
  private get loggedInKey(): string {
    return this._key('loggedIn');
  }

  private get notLoggedInKey(): string {
    return this._key('notLoggedIn');
  }

  private get userKey(): string {
    return this._key('user');
  }

  private get loginDataKey(): string {
    return this._key('loginData');
  }

  private get accessTokenKey(): string {
    return this._key('accessToken');
  }

  private get refreshTokenKey(): string {
    return this._key('refreshToken');
  }

  private get tokenTypeKey(): string {
    return this._key('tokenType');
  }

  private get rememberMeKey(): string {
    return this._key('rememberMe');
  }

  private get headerToBeSentKey(): string {
    return this._key('headerToBeSent');
  }
  // endregion
  // endregion
}
