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
   */
  @Input() heading = '';

  /**
   * Path to be linked in the "View details" button.
   */
  @Input() link = '';

  /**
   * Text to be shown in "View details" button.
   */
  @Input() linkText = 'View details »';

  /**
   * Preview text.
   */
  @Input() text = '';

  /**
   * Component constructor.
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() { }
}
