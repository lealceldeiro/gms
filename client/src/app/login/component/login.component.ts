import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { LoginService } from '../service/login.service';
import { SessionService } from '../../core/session/session.service';

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
   * User's password used for login.
   * @type {string}
   */
  password: string;

  /**
   * Whether the credentials should be stored or not.
   * @type {boolean}
   */
  rememberMe = true;

  /**
   * User's username or email used for login.
   * @type {string}
   */
  usernameOrEmail: string;

  /**
   * Component constructor
   * @param loginService LoginService for handling the login API requests.
   * @param sessionService SessionService for storing/retrieving session-related information.
   * @param router Router module in order to perform navigation.
   */
  constructor(private loginService: LoginService, private sessionService: SessionService, private router: Router) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.sessionService.isLoggedIn().subscribe(logged => {
      if (logged) {
        this.router.navigateByUrl('home');
      }
    });
  }

  /**
   * Performs a login request using the inputs values the user has typed in as username/email and password.
   */
  login(loginForm: NgForm | any): void {
    const payload: LoginRequestModel = { usernameOrEmail: this.usernameOrEmail, password: this.password };
    const ls = this.loginService.login(payload).subscribe((response: LoginResponseModel) => {
      ls.unsubscribe();
      this.sessionService.setRememberMe(this.rememberMe);
      this.router.navigateByUrl('home');
    }, () => <NgForm>loginForm.resetForm({ rememberMe: this.rememberMe }));
  }

}
