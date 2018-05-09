import { Component, OnInit } from '@angular/core';
import { SessionService } from '../core/session/session.service';
import { LoginRequestBody } from './shared/request';
import { LoginResponseBody } from './shared/response';

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
   * @param sessionService SessionService instance.
   */
  constructor(private sessionService: SessionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
  }

  /**
   * Performs a login request using the inputs values the user has typed in as username/email and password.
   */
  login(): void {
    const payload: LoginRequestBody = { usernameOrEmail: this.usernameOrEmail, password: this.password};
    this.sessionService.login(payload).subscribe((response: LoginResponseBody) => {
    });
  }

}
