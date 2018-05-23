import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';

/**
 * Component for generating the home page of the app.
 */
@Component({
  selector: 'gms-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  /**
   * Application name.
   * @type {string}
   */
  appName: string = environment.metaName;

  /**
   * Component constructor
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() { }

}
