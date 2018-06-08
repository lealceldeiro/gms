import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

/**
 * A service used for showing notifications to the user.
 */
@Injectable()
export class NotificationService {

  /**
   * Service constructor.
   * @param toastr {ToastrService} Service for showing the messages.
   */
  constructor(private toastr: ToastrService) { }

  /**
   * Shows an error message
   * @param {string} message Message body.
   * @param {string} title Message title.
   * @return {any}
   */
  error(message?: string, title?: string): any {
    return this.toastr.error(message, title);
  }
}
