import { Component, Input, OnInit } from '@angular/core';

/**
 * Component for generating a Bootstrap's Jumbotron.
 */
@Component({
  selector: 'gms-jumbotron',
  templateUrl: './jumbotron.component.html',
  styleUrls: ['./jumbotron.component.scss']
})
export class GmsJumbotronComponent implements OnInit {

  /**
   * The text to be shown as header.
   * @type {string}
   */
  @Input() header: string;

  /**
   * The path to which the button will be linked to.
   * @type {string}
   */
  @Input() link: string;

  /**
   * The text to be shown in the button.
   * @type {string}
   */
  @Input() linkText: string;

  /**
   * The bootstrap class which gives the button its size.
   * @type {string}
   */
  @Input() sizeClass = 'btn-lg';

  /**
   * The text to be shown.
   * @type {string}
   */
  @Input() text: string;

  /**
   * The bootstrap class which gives the button its color style.
   * @type {string}
   */
  @Input() typeClass = 'btn-primary';

  /**
   * Component constructor
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() { }

}
