import { Component, OnInit } from '@angular/core';

/**
 * Component for generating a (404) not found page. This is used when the user requests a path which is not defined in the routing.
 */
@Component({
  selector: 'gms-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})
export class PageNotFoundComponent implements OnInit {

  /**
   * Component constructor
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  ngOnInit() {
  }

}
