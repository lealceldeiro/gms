import { Component, Input, OnInit } from '@angular/core';

/**
 * Component used for generating a content preview.
 */
@Component({
  selector: 'gms-preview-content',
  templateUrl: './preview-content.component.html',
  styleUrls: ['./preview-content.component.scss']
})
export class PreviewContentComponent implements OnInit {

  /**
   * Heading text.
   * @type {string}
   */
  @Input() heading: string;

  /**
   * Path to be linked in the "View details" button.
   * @type {string}
   */
  @Input() link: string;

  /**
   * Text to be shown in "View details" button.
   * @type {string}
   */
  @Input() linkText = 'View details »';

  /**
   * Preview text.
   * @type {string}
   */
  @Input() text: string;

  /**
   * Component constructor.
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  ngOnInit() {
  }

}
