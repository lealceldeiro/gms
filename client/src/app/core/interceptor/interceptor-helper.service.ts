import { Injectable } from '@angular/core';

/**
 * Service for providing a communication between the interceptors and the rest of the app service in order
 * to share information.
 */
@Injectable()
export class InterceptorHelperService {

  /**
   * Excluded urls from being caught in order to handle any possible error.
   * @type {any[]}
   */
  private excludedFromErrorHandling: string[] = [];

  /**
   * Service constructor
   */
  constructor() { }

  /**
   * Indicates whether a url should be excluded or not from being intercepted.
   * @param {string} url Url to test against to.
   * @returns {boolean} Returns `true` if the url argument should be excluded, `false` otherwise.
   */
  isExcludedFromErrorHandling(url: string): boolean {
    let isExcluded = false;
    for (let i = 0; i < this.excludedFromErrorHandling.length; i++) {
      if (url.indexOf(this.excludedFromErrorHandling[i]) !== -1) { // if argument url contains some of the excluded string
        isExcluded = true;
        break;
      }
    }
    return isExcluded;
  }

  /**
   * Adds a url to the excluded urls collection in order to not be caught by the interceptor.
   * @param {string} url New url to be excluded.
   * @return {boolean} Returns `false` if the url argument was already in the excluded collection (and thus, not added
   * again), `true` otherwise.
   */
  addExcludedFromErrorHandling(url: string): boolean {
    const i = this.excludedFromErrorHandling.indexOf(url);
    if (i === -1) {
      this.excludedFromErrorHandling.push(url);
      return true;
    }
    return false;
  }

  /**
   * Removes a url from the excluded urls collection in order to not be caught by the interceptor.
   * @param {string} url New url to be excluded.
   * @return {boolean} Returns `true` if the url argument was already in the excluded collection, `false` otherwise.
   */
  removeExcludedFromErrorHandling(url: string): boolean {
    const i = this.excludedFromErrorHandling.indexOf(url);
    if (i !== -1) {
      this.excludedFromErrorHandling.splice(i, 1);
      return true;
    }
    return false;
  }
}
