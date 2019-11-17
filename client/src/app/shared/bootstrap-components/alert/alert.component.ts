import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

/**
 * Component for generating a Bootstrap's alert.
 */
@Component({
  selector: 'gms-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {
  /**
   * Alert heading/title. It will be shown in bold at the start of the alert message.
   */
  @Input() heading = '';

  /**
   * Alert message.
   */
  @Input() message = '';

  /**
   * If true, alert can be dismissed by the user.
   * The close button (×) will be displayed and you can be notified of the event with the (close) output.
   */
  @Input() dismissible = true;

  /**
   * Indicates whether the alert should be auto-dismiss after a period of time. That period of time is determined
   * by `this.dismissMilliseconds`.
   */
  @Input() autoDismissible = false;

  /**
   * If `this.autoDismissible` is true this value is used to determine when the alert should be dismissed.
   */
  @Input() dismissMilliseconds = 20000;

  /**
   * Type of the alert. Current available values: 'success', 'info', 'warning', 'danger', 'primary', 'secondary', 'light' and 'dark'.
   */
  @Input() type = 'info';

  @Input() closeMessage = '';

  /**
   * An event emitted when the close button is clicked. It has no payload and only relevant for dismissible alerts.
   */
  @Output() close = new EventEmitter();

  /**
   * Whether the alert should be closed or not. Useful for auto-closeable alerts.
   */
  shouldClosed = false;

  /**
   * Component constructor
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    if (this.autoDismissible) {
      setTimeout(
        () => {
          this.shouldClosed = true;
        },
        this.dismissMilliseconds
      );
    }
  }

  /**
   * Emits a notification when the alert is dismissed. The notification emitted is the message set on `closeMessage`.
   */
  onClose() {
    this.close.emit(this.closeMessage);
  }
}
