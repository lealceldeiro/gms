import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { excluded, routes } from '../app-routing.module';

/**
 * Component for generating a side menu.
 */
@Component({
  selector: 'gms-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements OnInit {
  /**
   * Indicates whether the nav bar is collapsed or not when is is in a resolution lower than the specified as
   * breakpoint.
   */
  isCollapsed = true;

  /**
   * Url the nav bar can navigate to.
   */
  urls: { path: string, name: string }[] = [];

  /**
   * Component constructor.
   */
  constructor(private router: Router) {
  }

  /**
   * Lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  ngOnInit() {
    this.urls = routes.map<{ path: string, name: string }>(r => (
      { path: r.path || 'not-found', name: this.getNameFrom(r.path) }
    ));
  }

  /**
   * Indicates whether the provided link is the one active or not.
   *
   * @param link Link to be checked.
   * @returns`true` if the link is the one active or `false` otherwise.
   */
  isLinkActive(link: string): boolean {
    return this.router.isActive(link, true);
  }

  /**
   * Returns a logical name from a given url. i.e: from /business-management/local/ it returns Business Management.
   * @param url Name to return from a given URL.
   */
  private getNameFrom(url: string | undefined | null): string {
    const base: string = url || 'Undefined';
    const relatives = this.getCleanUrls(base.split('/')); // get [business-management, local]
    let name = '';
    relatives.forEach(partialName => {
      const names = partialName.split('-'); // get [business, management]
      names.forEach(subName => {
        name += subName ? ' ' + this.getCapitalized(subName) : '';
      });
    });
    return name.slice(1);
  }

  /**
   * Capitalizes a string.
   * @param str String to be capitalized.
   */
  private getCapitalized(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  /**
   * Returns the menu urls from a source array with the urls. Specific rules are applied here in order
   * to determine whether a url is going to be part or not in the side menu.
   * @param urls Urls to be taken as source for creating the menu urls.
   */
  private getCleanUrls(urls: Array<string>): Array<string> {
    const copy: Array<string> = [];
    urls.forEach(url => {
      if (url.indexOf('*') === -1 && excluded.indexOf(url) === -1) {
        copy.push(url);
      }
    });
    return copy;
  }
}
