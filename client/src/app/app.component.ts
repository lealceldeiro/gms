import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';

import { Subscription } from 'rxjs';

import { SessionService } from './core/session/session.service';

/**
 * Main component which is the entry point to all other components in the app.
 */
@Component({
  selector: 'gms-shell',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  /**
   * Indicates whether the user is logged in or not.
   */
  public loggedIn = false;

  /**
   * Subscription to new values returned by SessionService#isLoggedIn.
   */
  private loggedInSub: Subscription = new Subscription();

  /**
   * Listens for the app when is being leaved and tries to remove the login data if the user has not chosen to be
   * remembered.
   *
   * @param $event Event triggered (unused for now.)
   */
  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($eventUnused: any) {
    this.sessionService.isRememberMe().subscribe((shouldIRememberSessionInfo: boolean) => {
      if (!shouldIRememberSessionInfo) {
        this.sessionService.closeSession();
      }
    });
  }

  /**
   * Component constructor.
   *
   * @param sessionService Service which holds session-related information.
   */
  constructor(public sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loggedInSub = this.sessionService.isLoggedIn().subscribe((logged) => {
      this.loggedIn = logged;
    });
    this.sessionService.loadInitialData();
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.loggedInSub.unsubscribe();
  }
}
