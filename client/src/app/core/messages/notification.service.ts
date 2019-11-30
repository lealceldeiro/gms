import { Injectable } from '@angular/core';

import { ToastrService } from 'ngx-toastr';

/**
 * A service used for showing notifications to the user.
 */
@Injectable()
export class NotificationService {
  /**
   * Service constructor.
   * @param toastr Service for showing the messages.
   */
  constructor(private toastr: ToastrService) { }

  /**
   * Shows an error message
   * @param message Message body.
   * @param title Message title.
   * @return the ActiveToast<any>
   */
  error(message?: string, title?: string): any {
    return this.toastr.error(message, title);
  }
}
