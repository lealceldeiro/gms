import { Component, OnInit } from '@angular/core';

/**
 * Generates a login component in order to allow users to login into the system
 */
@Component({
  selector: 'gms-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  /**
   * User's username or email used for login.
   */
  usernameOrEmail: string;

  /**
   * User's password used for login.
   */
  password: string;

  /**
   * Whether the credentials should be stored or not.
   */
  rememberMe = true;

  /**
   * Component constructor
   */
  constructor() { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
  }

  login(): void {

  }

}
