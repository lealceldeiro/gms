import { Injectable } from '@angular/core';

/**
 * Service for storing the possible url which resulted in  page not found.
 */
@Injectable()
export class PageNotFoundService {
  /**
   * Routes which resulted in a not found page.
   */
  notFoundUrls: Array<string> = [];

  /**
   * Service constructor
   */
  constructor() { }

  /**
   * Removes a url from the collection of url which resulted in a not found page.
   * @param {string} url Url to be removed.
   */
  removeUrl(url: string): void {
    const i = this.notFoundUrls.indexOf(url);
    if (i !== -1) {
      this.notFoundUrls.splice(i, 1);
    }
  }

  /**
   * Adds a url to the collection of url which resulted in a not found page.
   * @param {string} url Url to be added.
   */
  addUrl(url: string): void {
    const i = this.notFoundUrls.indexOf(url);
    if (i === -1) {
      this.notFoundUrls.push(url);
    }
  }

  /**
   * Returns whether the url was added previously as a not found route.
   * @param {string} url url to be checked against to.
   * @return {boolean} `true` if it was added previously as a not found route, `false` otherwise.
   */
  wasNotFound(url: string): boolean {
    return this.notFoundUrls.indexOf(url) !== -1;
  }
}
