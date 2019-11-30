import { Injectable } from '@angular/core';

/**
 * Service for providing a communication between the interceptors and the rest of the app service in order
 * to share information.
 */
@Injectable()
export class InterceptorHelperService {
  /**
   * Excluded urls from being caught in order to handle any possible error.
   */
  private excludedFromErrorHandling: string[] = [];

  /**
   * Service constructor
   */
  constructor() { }

  /**
   * Indicates whether a url should be excluded or not from being intercepted.
   * @param url Url to test against to.
   * @returns Returns `true` if the url argument should be excluded, `false` otherwise.
   */
  isExcludedFromErrorHandling(url: string): boolean {
    let isExcluded = false;
    for (const excluded of this.excludedFromErrorHandling) {
      if (url.indexOf(excluded) !== -1) { // if argument url contains some of the excluded string
        isExcluded = true;
        break;
      }
    }
    return isExcluded;
  }

  /**
   * Adds a url to the excluded urls collection in order to not be caught by the interceptor.
   * @param url New url to be excluded.
   * @return Returns `false` if the url argument was already in the excluded collection (and thus, not added
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
   * @param url New url to be excluded.
   * @return Returns `true` if the url argument was already in the excluded collection, `false` otherwise.
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
