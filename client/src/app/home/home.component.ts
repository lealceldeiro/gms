import { Component, OnDestroy, OnInit } from '@angular/core';

import { Subscription } from 'rxjs';

import { AppConfig } from '../core/config/app.config';
import { SessionService } from '../core/session/session.service';

/**
 * Component for generating the home page of the app.
 */
@Component({
  selector: 'gms-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  /**
   * Application name.
   */
  appName = AppConfig.settings.env.metaName;

  /**
   * Indicates whether the user is logged in or not.
   */
  isLoggedIn = false;

  /**
   * Subscription to the logged in status.
   */
  isLoggedInSubscription = new Subscription();

  /**
   * Component constructor
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.isLoggedInSubscription = this.sessionService.isLoggedIn().subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
    });
  }

  /**
   * Lifecycle hook that is called hen the component is destroyed.
   */
  ngOnDestroy() {
    this.isLoggedInSubscription.unsubscribe();
  }
}
