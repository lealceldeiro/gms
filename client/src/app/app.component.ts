import { Component } from '@angular/core';

/**
 * Main component which is the entry point to all other components in the app.
 */
@Component({
  selector: 'gms-shell',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  /**
   * The client app title.
   *
   * @type {string}
   */
  title = 'General Management System';
}
