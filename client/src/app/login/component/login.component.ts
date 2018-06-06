import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpResponseBase } from '@angular/common/http';

import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginService } from '../service/login.service';
import { SessionService } from '../../core/session/session.service';
import { FormHelperService } from '../../core/form/form-helper.service';
import { HTTP_STATUS_UNAUTHORIZED } from '../../core/response/http-status';

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
   * Form for handling the login data inputted by the user.
   */
  loginForm: FormGroup;

  /**
   * Whether the login form was submitted or not.
   * @type {boolean}
   */
  submitted = false;

  /**
   * Component constructor
   * @param loginService LoginService for handling the login API requests.
   * @param sessionService SessionService for storing/retrieving session-related information.
   * @param router Router module in order to perform navigation.
   * @param fb FormBuilder A form builder for generating the login form.
   * @param formHelperService FormHelperService.
   * @param toastr ToastrService Service for showing notifications to the user.
   */
  constructor(private loginService: LoginService, private sessionService: SessionService, private router: Router,
              private fb: FormBuilder, private formHelperService: FormHelperService, private toastr: ToastrService) {
    this.createLoginForm();
  }

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
  login(): void {
    const rm = this.formValueOf('rememberMe');
    if (this.loginForm.valid) {
      this.submitted = true;
      const payload: LoginRequestModel = {
        usernameOrEmail: this.formValueOf('usernameOrEmail'),
        password: this.formValueOf('password')
      };
      const ls = this.loginService.login(payload).subscribe(() => {
        ls.unsubscribe();
        this.sessionService.setRememberMe(rm);
        this.router.navigateByUrl('home');
      }, (response: HttpResponseBase) => {
        if (response.status === HTTP_STATUS_UNAUTHORIZED) {
          this.toastr.error('Wrong credentials', 'Login Failed');
        }
        this.loginForm.reset({rememberMe: rm});
        this.submitted = false;
      });
    } else {
      this.formHelperService.markFormElementsAsTouched(this.loginForm);
    }
  }

  /**
   * Creates the login (reactive) form.
   */
  private createLoginForm() {
    this.loginForm = this.fb.group({
      usernameOrEmail: [ null, Validators.required ],
      password: [ null, Validators.required ],
      rememberMe: true
    });
  }

  /**
   * Returns the value set for a form control in the login form.
   * @param {string} name
   * @returns {any}
   */
  private formValueOf(name: string): any {
    return this.loginForm.get(name).value;
  }

}
