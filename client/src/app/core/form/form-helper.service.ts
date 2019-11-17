import { Injectable } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';

/**
 * A helper service for common tasks related to angular forms.
 */
@Injectable()
export class FormHelperService {
  /**
   * Service constructor
   */
  constructor() { }

  /**
   * Marks all controls in a form as touched.
   * @param {FormGroup | NgForm} form
   */
  markFormElementsAsTouched(form: FormGroup | NgForm): void {
    Object.keys(form.controls).forEach(ctrlName => {
      form.controls[ctrlName].markAsTouched();
    });
  }
}
