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
      // before saving to localStorage, serialize
    } else {
      this.cache[key] = value;
    }
    return value;
  }

  get(key: string): any {
    if (typeof key === 'undefined' || key === null) {
      throw new Error('The key must be defined');
    }
    const value = this.cache[key];
    if (!value) {
      // if not in cache, get from localStorage
    }
    return JSON.parse(value);
  }

  clear(key?: string): any {
    if (typeof key !== 'undefined' && typeof key !== null) {
      const value = this.get(key);
      deleteProperty(this.cache, key);
      return value;
    }
    this.cache = {};
  }
}
