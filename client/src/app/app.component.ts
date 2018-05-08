import { Component } from '@angular/core';
import { SessionService } from './core/session/session.service';

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
   * Component constructor.
   * @param {SessionService} sessionService Service which holds session-related information.
   */
  constructor(public sessionService: SessionService) {
  }
}
