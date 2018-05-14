import { Injectable } from '@angular/core';
import deleteProperty = Reflect.deleteProperty;

/**
 * A service for providing access to the storage in client runner (browser, etc).
 */
@Injectable()
export class StorageService {

  /**
   * Object which holds the stored values.
   * @type {{}}
   */
  private cache = {};

  /**
   * Service constructor.
   */
  constructor() { }

  /**
   * Sets a new value under a key.
   * @param {string} key String representation of the key under which the value will be stored.
   * @param value Value to be stored
   * @returns {any}
   */
  set(key: string, value: any): any {
    if (typeof key === 'undefined' || key === null) {
      throw new Error('The key must be defined');
    }
    if (typeof value === 'object') {
      this.cache[key] = value;
      // before saving to local storage, serialize
    } else {
      this.cache[key] = value;
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
    if (typeof key === 'undefined' || key === null) {
      throw new Error('The key must be defined');
    }
    const value = this.cache[key];
    if (typeof value === 'undefined' || value === null) {
      // if not in cache, get from localStorage
    }
    if (typeof value === 'undefined' || value === null) {
      return null;
    }
    return JSON.parse(value);
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
      deleteProperty(this.cache, key);
      // delete from local storage

      return value;
    }
    this.cache = {};
    return null;
  }
}
