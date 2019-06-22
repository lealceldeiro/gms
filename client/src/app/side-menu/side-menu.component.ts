import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

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
   * Indicates whether the nav bar is collapsed or not when is is in a resolution lower than the specified as breakpoint.
   */
  isCollapsed = true;

  /**
   * Url the nav bar can navigate to.
   */
  urls = [
    { path: '/entities', name: 'Owned Entities' },
    { path: '/users', name: 'Users' },
    { path: '/roles', name: 'Roles' },
    { path: '/permissions', name: 'Permissions' },
    { path: '/configurations', name: 'Configurations' },
  ];

  /**
   * Component constructor.
   */
  constructor(private router: Router) { }

  /**
   * Lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  ngOnInit() { }

  /**
   * Indicates whether the provided link is the one active or not.
   *
   * @param {string} link Link to be checked.
   * @returns {boolean} `true` if the link is the one active or `false` otherwise.
   */
  isLinkActive(link: string): boolean {
    return this.router.isActive(link, true);
  }
}
