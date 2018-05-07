import { Component, OnInit } from '@angular/core';
import { appMeta } from '../../environments/environment';

/**
 * Component for generating the home page of the app.
 */
@Component({
  selector: 'gms-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  appName: string = appMeta.name;

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
