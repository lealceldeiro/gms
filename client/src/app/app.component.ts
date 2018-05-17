import { Component, OnDestroy, OnInit } from '@angular/core';
import { SessionService } from './core/session/session.service';
import { Subscription } from 'rxjs/index';

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
   * {boolean}
   */
  public loggedIn = false;

  /**
   * Observable for subscribing to new values returned by SessionService#isLoggedIn.
   */
  private loggedIn$: Subscription;

  /**
   * Component constructor.
   * @param {SessionService} sessionService Service which holds session-related information.
   */
  constructor(public sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loggedIn$ = this.sessionService.isLoggedIn().subscribe((logged) => {
      this.loggedIn = logged;
      console.log('cp: ' + this.loggedIn);
    });
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.loggedIn$.unsubscribe();
  }
}
