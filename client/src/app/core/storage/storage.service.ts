import { Injectable } from '@angular/core';
import { CookieOptions, CookieService } from 'ngx-cookie';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { tap } from 'rxjs/internal/operators';
import { BehaviorSubject, Observable, of } from 'rxjs/index';

/**
 * A service for providing access to the storage and cookies in client runner (browser, etc).
 */
@Injectable()
export class StorageService {

  /**
   * Prefix for all keys used for storing values in cookies
   * @type {string}
   */
  private gmsCk = 'gms_ck_';

  /**
   * Prefix for all keys used for storing values in localStorage
   * @type {string}
   */
  private gmsLs = 'gms_ls_';

  /**
   * Prefix keys for values saved under a private key.
   * @type {string}
   */
  private gmsPriv = 'priv_';

  /**
   * Object which holds the stored values for localStorage.
   * @type {{}}
   */
  private cache = {};

  /**
   * Observables of StorageService#cache
   * @type {{}}
   */
  private cache$ = {};

  /**
   * Object which holds the stored values for cookies.
   * @type {{}}
   */
  private cacheCk = {};

  /**
   * Observables of StorageService#cacheCk
   * @type {{}}
   */
  private cacheCk$ = {};

  /**
   * Utility subject for geenrating an observable for returning in function in order to wait until the end of
   * the operation.
   * @type {BehaviorSubject<boolean>}
   * @see booleanSubj$
   */
  private booleanSubj = new BehaviorSubject<boolean>(true);

  /**
   * Utility observable for returning in function in order to wait until the end of the operation.
   * @type {Observable<boolean>}
   */
  private booleanSubj$ = this.booleanSubj.asObservable();

  /**
   * Object for storing how many times StorageService#trySet function have been trying to save a particular value.
   * @type {}
   */
  private trySetCount = {};

  /**
   * Object for storing how many times StorageService#tryClear function have been trying to clear a particular value.
   * @type {}
   */
  private tryClearCount = {};

  /**
   * Service constructor.
   * @param {CookieService} cookieService CookieService for storing values in cookies.
   * @param {LocalStorage} localStorage LocalStorage for storing values in the browser local storage.
   */
  constructor(private cookieService: CookieService, private localStorage: LocalStorage) { }

  // region local storage
  /**
   * Sets a new value under a key in the localStorage.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   * @returns {any}
   */
  set(key: string, value: any): any {
    this.checkKey(key);
    const uk = this.gmsLs + key;
    this.setCache(uk, value);
    this.trySetCount[uk] = 0;
    this.trySet(uk, value);
    return value;
  }

  /**
   * Tries to save a value under a key in the client local storage. If the method fails it will retry 3 times more to
   * save it.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   */
  private trySet(key: string, value: any): void {
    this.localStorage.setItem(key, value).subscribe(() => {}, () => {
      if (this.trySetCount[key]++ < 2) {
        this.trySet(key, value);
      } else {
        console.warn('Couldn\'t set ' + value + ' under key \'' + key + '\'');
      }
    });
  }

  /**
   * Returns an observable which will emit the value specified under a key.
   * @param {string} key Key under which the value it's being tried to be accessed was saved previously.
   * @returns {Observable<any>} An Observable with the saved value under the specified key or `null` if no value is
   * found under the specified.
   * key.
   */
  get(key: string): Observable<any> {
    this.checkKey(key);
    const uk = this.gmsLs + key;
    const value$ = this.cache$[uk];
    return value$ ? value$ : this.localStorage.getItem(uk).pipe(tap((val) => this.setCache(uk, val),
      () => console.warn('Couldn\'t get value under key \'' + key + '\'')));
  }

  /**
   * Creates the cache value and the cache observable.
   * @param {string} key Key for looking up.
   * @param {any} val Value to be looked up.
   * @param {boolean} isCookie Whether to set the value in the cookie cache or the localStorage cache.
   */
  private setCache(key: string, val: any, isCookie = false): void {
    let subject = isCookie ? this.cacheCk[key] : this.cache[key];
    if (!subject) {
      subject = new BehaviorSubject(val);
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
   * Sets all values in cache to null.
   * @param {boolean} isCookie Whether the cache is going to be cleared is the cookie or localStorage cache.
   */
  private clearCache(isCookie = false) {
    if (isCookie) {
      for (const k in this.cacheCk) {
        if (this.cacheCk.hasOwnProperty(k)) {
          this.cacheCk[k].next(null);
        }
      }
    } else {
      for (const k in this.cache) {
        if (this.cache.hasOwnProperty(k)) {
          this.cache[k].next(null);
        }
      }
    }
  }

  /**
   * Clears a value under a key or all values if no key is specified. This function uses the StorageService#get function
   * in order to return the value.
   * @param {string} key Key under the value is being tried to be removed was saved previously. If no key is provided
   * all values are removed.
   * @returns {Observable<boolean>}
   */
  clear(key?: string): Observable<boolean> {
    this.tryClearCount[this.gmsLs + key] = 0;
    return this.tryClear(key);
  }

  /**
   * Tries to clear a value under a key or all values if no key is specified. This function uses the StorageService#get
   * function in order to return the value. If the method fails it will retry 3 times more to clear the value.
   * @param {string} key
   * @returns {Observable<boolean>}
   */
  private tryClear(key?: string): Observable<boolean> {
    // clear a specific value
    if (typeof key !== 'undefined' && typeof key !== null) {
      const uk = this.gmsLs + key;
      return this.localStorage.removeItem(uk).pipe(tap( (nVal) => {
        if (nVal !== null) {
          // this will trigger the next value of the observable in this.cache$[uk]
          this.cache[uk].next(null);
        }
      }, () => {
        if (this.tryClearCount[uk]++ < 2) {
          return this.tryClear(key);
        } else {
          console.warn('Couldn\'t delete value under key \'' + key + '\'');
        }
      }));
    } else { // clear all
      return this.localStorage.clear().pipe(tap(() => {
        this.clearCache();
      }, () => {
        if (this.tryClearCount[key]++ < 2) {
          return this.tryClear(key);
        } else {
          console.warn('Couldn\'t delete value under key \'' + key + '\'');
        }
      }));
    }
  }
  // endregion

  // region cookies
  /**
   * Puts a new value under a key in a cookie.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored.
   * @param {object} options Additional options for cookies.
   * @param {object} options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <gmsCk><code>{expires: <value> {string|Date}</code></gmsCk>
   * @returns {any}
   */
  putCookie(key: string, value: any, options?: object): any {
    this.checkKey(key);
    const uk = this.gmsCk + key;
    this.setCache(uk, value, true);
    typeof value === 'object' ? this.cookieService.putObject(uk, value, options as CookieOptions)
      : this.cookieService.put(uk, value);
    return value;
  }

  /**
   * Gets a cookie value (or all values if no key is provided) specified under a key stored in cookie.
   * @param {string} key Key under which the value it's being tried to be accessed was saved previously.If not key is
   * provided or a falsy value, this returns all cookies.
   * @param {boolean} isObject Whether the value is trying to be retrieved is an object or not.
   * @returns {Observable<any>} An Observable with the saved value under the specified key (or all values if no key is
   * provided) or `null` if no value is found under the specified key.
   */
  getCookie(key?: string, isObject = false): Observable<any> {
    let uk;
    if (key) {
      uk = this.gmsCk + key;
      let value$ = this.cacheCk$[uk];
      if (!value$) {
        const val = isObject ? this.cookieService.getObject(uk) : this.cookieService.get(uk);
        this.setCache(uk, val, true);
      }
      value$ = this.cacheCk$[uk];
      return value$ ? value$ : of(null);
    } else {
      uk = this.gmsPriv + this.gmsCk + key;
      const subject = this.cacheCk[uk];
      const all = this.cookieService.getAll();
      if (!subject) {
        this.cacheCk[uk] = new BehaviorSubject<Object>(all);
        this.cacheCk$[uk] = this.cacheCk[uk].asObservable();
      } else {
        this.cacheCk[uk].next(all);
      }
      return this.cacheCk$[uk];
    }
  }

  /**
   * Clears a value under a key or all values if no key is specified or its value is falsy. This function uses the
   * StorageService#getCookie function in order to return the value.
   * @param {string} key Key under the value is being tried to be removed was saved previously. If no key is provided
   * all values are removed.
   * provided, then this can not be provided.
   * @returns {Observable<boolean>} An Observable to wait the end of the operation.
   */
  clearCookie(key?: string): Observable<boolean> {
    if (key) {
      const uk = this.gmsCk + key;
      this.cookieService.remove(uk);
      const subject = this.cacheCk[uk];
      if (subject) {
        this.cacheCk[uk].next(null);
      }
    } else {
      this.clearCache(true);
    }
    return this.booleanSubj$;
  }
  // endregion

  /**
   * Checks whether a given key is valid or not. This throws an error if the given key is invalid.
   * @param {string} key
   */
  private checkKey(key: string) {
    if (typeof key === 'undefined' || key === null) {
      throw new Error('The key must be defined');
    }
  }
}
