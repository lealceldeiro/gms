import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from '../../environments/environment';
import { SessionService } from '../core/session/session.service';
import { Subscription } from 'rxjs';

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
   * @type {string}
   */
  appName: string = environment.metaName;

  /**
   * Indicates whether the user is logged in or not.
   * @type {boolean}
   */
  isLoggedIn = false;

  /**
   * Subscription to the logged in status.
   * @type {Subscription}
   */
  isLoggedInSubscription: Subscription;

  /**
   * Component constructor
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.isLoggedInSubscription = this.sessionService.isLoggedIn().subscribe(ili => this.isLoggedIn = ili);
  }

  /**
   * Lifecycle hook that is called hen the component is destroyed.
   */
  ngOnDestroy() {
    this.isLoggedInSubscription.unsubscribe();
  }

}
