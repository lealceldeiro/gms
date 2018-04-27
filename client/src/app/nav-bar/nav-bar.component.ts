import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

/**
 * Component for generating a Bootstrap's NavBar.
 */
@Component({
  selector: 'gms-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  /**
   * Indicates whether the nav bar is collapsed or not when is is in a resolution lower than the specified as breakpoint.
   * @type {boolean}
   */
  isCollapsed = true;

  /**
   * Component constructor
   *
   * @param router Router component for handling routes.
   */
  constructor(private router: Router) { }


  /**
   * Lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  ngOnInit() {
  }

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
