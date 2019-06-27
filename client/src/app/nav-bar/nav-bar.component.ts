import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/index';
import { SessionService } from '../core/session/session.service';
import { User } from '../core/session/user.model';

/**
 * Component for generating a Bootstrap's NavBar.
 */
@Component({
  selector: 'gms-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit, OnDestroy {

  /**
   * Indicates whether the nav bar is collapsed or not when is is in a resolution lower than the specified as breakpoint.
   */
  isCollapsed = true;
  /**
   * Indicates whether the user is logged in or not.
   */
  loggedIn = false;
  /**
   * Session user's info.
   */
  user = new User();

  // region @Input
  /**
   * Indicates whether the nav bar search form is active or not.
   */
  @Input() isSearchActive = true;
  /**
   * Search input placeholder.
   */
  @Input() searchPlaceholder = 'Type here to search ...';
  /**
   * Search button text.
   */
  @Input() searchText = 'Search';
  // endregion

  /**
   * Available options to on how to perform the searches.
   */
  searchOptions = ['Contains', 'Equals', 'Anything'];

  /**
   * Selected option when doing a search.
   */
  selectedSearchOption = 'Anything';

  /**
   * Term used to perfom a search.
   */
  searchTerm = null;

  /**
   * URLs where the nav bar can navigate to.
   */
  urls = [
    { path: '/help', name: 'Help' },
    { path: '/about', name: 'About' }
  ];

  /**
   * Observable for subscribing to new values returned by SessionService#isLoggedIn.
   */
  private loggedInSub = new Subscription();

  /**
   * Observable for subscribing to new values returned by SessionService#getUser.
   */
  private userSub = new Subscription();

  /**
   * Component constructor
   *
   * @param router Router component for handling routes.
   * @param sessionService SessionUserService for handling session-related information.
   */
  constructor(private router: Router, private sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loggedInSub = this.sessionService.isLoggedIn().subscribe((logged: boolean) => this.loggedIn = logged);
    this.userSub = this.sessionService.getUser().subscribe((userInfo: User) => this.user = userInfo);
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.loggedInSub.unsubscribe();
    this.userSub.unsubscribe();
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

  /**
   * Logs out the user from the current session.
   */
  logout(): void {
    this.sessionService.closeSession();
  }

  /**
   * Makes a search action to happen in the current view.
   */
  search() {

  }

}
