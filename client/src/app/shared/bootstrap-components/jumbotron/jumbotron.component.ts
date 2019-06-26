import { Component, Input, OnInit } from '@angular/core';

/**
 * Component for generating a Bootstrap's Jumbotron.
 */
@Component({
  selector: 'gms-jumbotron',
  templateUrl: './jumbotron.component.html',
  styleUrls: ['./jumbotron.component.scss']
})
export class JumbotronComponent implements OnInit {

  /**
   * The text to be shown as header.
   */
  @Input() header = '';

  /**
   * The path to which the button will be linked to.
   */
  @Input() link = '';

  /**
   * The text to be shown in the button.
   */
  @Input() linkText = '';

  /**
   * The bootstrap class which gives the button its size.
   */
  @Input() sizeClass = 'btn-lg';

  /**
   * The text to be shown.
   */
  @Input() text = '';

  /**
   * The bootstrap class which gives the button its color style.
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
