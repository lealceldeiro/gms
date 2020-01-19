import { Injectable } from '@angular/core';

import { LocalStorage } from '@ngx-pwa/local-storage';
import { CookieOptions, CookieService } from 'ngx-cookie';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { TruthyPredicate } from '../predicate/truthy.predicate';
import { Util } from '../util/util';

/**
 * A service for providing access to the storage and cookies in client runner (browser, etc).
 */
@Injectable()
export class StorageService {
  /**
   * Object which holds the stored values for localStorage.
   */
  private cache: { [key: string]: any } = {};

  /**
   * Observables of StorageService#cache
   */
  private cache$: { [key: string]: any } = {};

  /**
   * Object which holds the stored values for cookies.
   */
  private cacheCk: { [key: string]: any } = {};

  /**
   * Observables of StorageService#cacheCk
   */
  private cacheCk$: { [key: string]: any } = {};

  /**
   * Utility subject for generating an observable for returning in function in order to wait until the end of
   * the operation.
   * @see booleanSubj$
   */
  private booleanSubj = new BehaviorSubject<boolean>(true);

  /**
   * Utility observable for returning in function in order to wait until the end of the operation.
   */
  private booleanSubj$ = this.booleanSubj.asObservable();

  /**
   * Object for storing how many times StorageService#trySet function have been trying to save a particular value.
   */
  private trySetCount: { [key: string]: number } = {};

  /**
   * Object for storing how many times StorageService#tryClear function have been trying to clear a particular value.
   */
  private tryClearCount: { [key: string]: number } = {};

  /**
   * Service constructor.
   *
   * @param cookieService CookieService for storing values in cookies.
   * @param localStorage LocalStorage for storing values in the browser local storage.
   */
  constructor(private cookieService: CookieService, private localStorage: LocalStorage) {
  }

  // region local storage
  /**
   * Sets a new value under a key in the localStorage.
   *
   * @param key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   * @returns The value if it was successfully set.
   */
  set(key: string, value: any): any {
    if (!this.isValidKey(key)) {
      return null;
    }

    this.setCache(key, value);
    this.trySetCount[key] = 0;
    this.trySet(key, value);
    return value;
  }

  /**
   * Tries to save a value under a key in the client local storage. If the method fails it will retry 2 times more to
   * save it.
   *
   * @param key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   */
  private trySet(key: string, value: any): void {
    this.localStorage.setItem(key, value).subscribe(() => {
    }, () => {
      if (this.trySetCount[key]++ < 2) {
        this.trySet(key, value);
      } else {
        console.warn(`Couldn't set ${ value } under key ${ key }`);
      }
    });
  }

  /**
   * Returns an observable which will emit the value specified under a key.
   *
   * @param key Key under which the value it's being tried to be accessed was saved previously.
   * @returns An Observable<any> with the saved value under the specified key or `null` if no value is
   * found under the specified.
   * key.
   */
  get(key: string): Observable<any> {
    const value$ = this.cache$[key];
    return value$ || this.localStorage.getItem(key).pipe(tap((val) => this.setCache(key, val)));
  }

  /**
   * Creates the cache value and the cache observable.
   *
   * @param key Key for looking up.
   * @param val Value to be looked up.
   * @param isCookie Whether to set the value in the cookie cache or the localStorage cache.
   */
  private setCache(key: string, val: any, isCookie = false): void {
    let subject = isCookie ? this.cacheCk[key] : this.cache[key];
    if (!subject) {
      subject = new BehaviorSubject<any>(val);
      if (isCookie) {
        this.cacheCk[key] = subject;
        this.cacheCk$[key] = this.cacheCk[key].asObservable();
      } else {
        this.cache[key] = subject;
        this.cache$[key] = this.cache[key].asObservable();
      }
    } else {
      if (isCookie) {
        this.cacheCk[key].next(val);
      } else {
        this.cache[key].next(val);
      }
    }
  }

  /**
   * Clears a value under a key. This function uses the StorageService#get function in order to return the value.
   *
   * @param key Key under the value is being tried to be removed was saved previously.
   * @returns Observable<boolean>
   */
  clear(key: string): Observable<boolean> {
    this.tryClearCount[key] = 0;
    return this.tryClear(key);
  }

  /**
   * Tries to clear a value under a key or all values if no key is specified. This function uses the StorageService#get
   * function in order to return the value. If the method fails it will retry 2 times more to clear the value.
   *
   * @param string key
   * @returns Observable<boolean>
   */
  private tryClear(key: string): Observable<boolean> {
    return this.localStorage.removeItem(key).pipe(tap(
      (removed) => {
        if (removed) {
          this.cache[key].next(null);
        }
      }, // null for next value in this.cache$[key]
      () => {
        this.tryClearCount[key]++ < 2 ? this.tryClear(key) : console.warn(`Couldn't delete value for ${ key }`);
      }
    ));
  }

  // endregion

  // region cookies
  /**
   * Puts a new value under a key in a cookie.
   *
   * @param key String representation of the key under which the value will be stored.
   * @param value Value to be stored.
   * @param options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <gmsCk><code>{expires: <value> {string|Date}</code></gmsCk>
   * @returns any
   */
  putCookie(key: string, value: any, options?: object): any {
    if (!this.isValidKey(key)) {
      return null;
    }

    this.setCache(key, value, true);
    typeof value === 'object' ? this.cookieService.putObject(key, value, options as CookieOptions)
      : this.cookieService.put(key, value);

    return value;
  }

  /**
   * Gets a cookie value (or all values if no key is provided) specified under a key stored in cookie.
   *
   * @param key Key under which the value it's being tried to be accessed was saved previously.
   * @param isObject Whether the value is trying to be retrieved is an object or not.
   * @returns An Observable<any> with the saved value under the specified key or `null` if
   * no value is found under the specified key.
   */
  getCookie(key: string, isObject = false): Observable<any> {
    let value$ = this.cacheCk$[key];
    if (!value$) {
      const val = isObject ? this.cookieService.getObject(key) : this.cookieService.get(key);
      this.setCache(key, val, true);
    }
    value$ = this.cacheCk$[key];

    return value$;
  }

  /**
   * Clears a value under a key. This function uses the StorageService#getCookie function in order to return the value.
   *
   * @param key Key under the value is being tried to be removed was saved previously. If no key is provided
   * all values are removed.
   * provided, then this can not be provided.
   * @returns An Observable<boolean> to wait the end of the operation.
   */
  clearCookie(key: string): Observable<boolean> {
    if (this.isValidKey(key)) {
      this.cookieService.remove(key);
      const subject = this.cacheCk[key];
      if (subject) {
        this.cacheCk[key].next(null);
      }
    }

    return this.booleanSubj$;
  }

  // endregion

  /**
   * Checks whether a given key is valid or not. This throws an error if the given key is invalid.
   * @param key Key which is validity is checked.
   */
  private isValidKey(key: string): boolean {
    return Util.allValuesFulfil(new TruthyPredicate<string>(), key);
  }
}
