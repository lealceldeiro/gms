import { Injectable } from '@angular/core';
import { CookieOptions, CookieService } from 'ngx-cookie';
import deleteProperty = Reflect.deleteProperty;

/**
 * A service for providing access to the storage in client runner (browser, etc).
 */
@Injectable()
export class StorageService {

  /**
   * Prefix for all keys used for storing values (either in cookies or in localStorage)
   * @type {string}
   */
  pre = 'gms_ck_';

  /**
   * Object which holds the stored values.
   * @type {{}}
   */
  private cache = {};

  /**
   * Service constructor.
   * @param {CookieService} cookieService CookieService for storing values in cookies.
   */
  constructor(private cookieService: CookieService) { }

  /**
   * Sets a new value under a key in the localStorage.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   * @returns {any}
   */
  set(key: string, value: any): any {
    this.checkKey(key);
    this.cache[this.pre + key] = value;
    if (typeof value === 'object') {
      // before saving to local storage, serialize
    } else {
      // save to local storage
    }
    return value;
  }

  /**
   * Gets a value specified under a key.
   * @param {string} key Key under which the value it's being tried to be accessed was saved previously.
   * @returns {any} The saved value under the specified key or `null` if no value is found under the specified key.
   */
  get(key: string): any {
    this.checkKey(key);
    const value = this.cache[this.pre + key];
    if (typeof value === 'undefined' || value === null) {
      // if not in cache, get from localStorage
    }
    return typeof value === 'undefined' || value === null ? null : JSON.parse(value);
  }

  /**
   * Clears a value under a key or all values if no key is specified. This function uses the StorageService#get function in order to return
   * the value.
   * @param {string} key Key under the value is being tried to be removed was saved previously. If no key is provided all values are removed
   * .
   * @returns {any} If a key is provided, the value that was saved under that key is returned or `null` if no key is provided.
   */
  clear(key?: string): any {
    if (typeof key !== 'undefined' && typeof key !== null) {
      const value = this.get(key);
      deleteProperty(this.cache, this.pre + key);
      // delete from local storage

      return value;
    }
    this.cache = {};
    return null;
  }

  /**
   * Puts a new value under a key in a cookie.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored.
   * @param {object} options Additional options for cookies.
   * @param {object} options Additional options to be passes. i.e.: if it is a cookie the 'expires' option can be set like this:
   * <pre><code>{expires: <value> {string|Date}</code></pre>
   * @returns {any}
   */
  putCookie(key: string, value: any, options?: object): any {
    this.checkKey(key);
    this.cache[this.pre + key] = value;
    typeof value === 'object' ? this.cookieService.putObject(this.pre + key, value, options as CookieOptions)
      : this.cookieService.put(this.pre + key, value);
    return value;
  }

  /**
   * Gets a cookie value (or all values if no key is provided) specified under a key stored in cookie.
   * @param {string} key Key under which the value it's being tried to be accessed was saved previously. If not key is provided or a falsy
   * value, this returns all cookies.
   * @param {boolean} isObject Whether the value is trying to be retrieved is an object or not.
   * @returns {any} The saved value under the specified key (or all values if no key is provided) or `null` if no value is found under the
   * specified key.
   */
  getCookie(key?: string, isObject = false): any {
    if (key) {
      let value = this.cache[this.pre + key];
      if (typeof value === 'undefined' || value === null) {
        value = isObject ? this.cookieService.getObject(this.pre + key) : this.cookieService.get(this.pre + key);
      }
      return value;
    } else {
      return this.cookieService.getAll();
    }
  }

  /**
   * Clears a value under a key or all values if no key is specified or its value is falsy. This function uses the StorageService#getCookie
   * function in order to return the value.
   * @param {string} key Key under the value is being tried to be removed was saved previously. If no key is provided all values are removed
   * .
   * @param {boolean} isObject Whether the value is being tried to be cleared is an object or not. If no key is provided, then this can not
   * be provided.
   * @returns {any} If a key is provided, the value that was saved under that key is returned or `null` if no key is provided or its value
   * is falsy.
   */
  clearCookie(key?: string, isObject = false): any {
    if (key) {
      const value = this.getCookie(key, isObject);
      deleteProperty(this.cache, this.pre + key);
      this.cookieService.remove(this.pre + key);
      return value;
    } else {
      this.cache = {};
      this.cookieService.removeAll();
      return null;
    }
  }

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
