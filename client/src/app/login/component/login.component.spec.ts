import { HttpErrorResponse } from '@angular/common/http';
import { DebugElement } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, Subject } from 'rxjs';

import { FormHelperService } from '../../core/form/form-helper.service';
import { NotificationService } from '../../core/messages/notification.service';
import { HttpStatusCode } from '../../core/response/http-status-code.enum';
import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { SessionService } from '../../core/session/session.service';
import { DummyStubComponent } from '../../shared/test-util/mock/dummy-stub.component';
import { MockModule } from '../../shared/test-util/mock/mock.module';
import { SharedModule } from '../../shared/shared.module';
import { LoginService } from '../service/login.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  const routes = [{ path: 'home', component: DummyStubComponent }];
  const text = 'sampleText';
  const sampleReq: LoginRequestModel = { password: text, usernameOrEmail: text };
  const sampleRes: LoginResponseModel = { username: 'testUser', token_type: 'testToken' };
  const subjectLoginResponseModel = new Subject<LoginResponseModel>();

  const sessionServiceIsLoggedInSubject = new Subject<boolean>();
  const sessionServiceStub = { isLoggedIn: () => sessionServiceIsLoggedInSubject.asObservable(), setRememberMe: () => { } };
  const formHelperStub = { markFormElementsAsTouched: () => { } };

  let component: LoginComponent;
  let componentEl: HTMLElement;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<LoginComponent>;
  let navigateByUrlSpy: jasmine.Spy;

  let loginServiceSpy: jasmine.SpyObj<LoginService>;
  let notificationSpy: jasmine.SpyObj<NotificationService>;

  beforeEach(async(() => {
    loginServiceSpy = jasmine.createSpyObj('LoginService', ['login']);
    loginServiceSpy.login.and.returnValue(subjectLoginResponseModel.asObservable());
    sessionServiceIsLoggedInSubject.next(false);

    notificationSpy = jasmine.createSpyObj('NotificationService', ['error']);

    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [MockModule, SharedModule, RouterTestingModule.withRoutes(routes)],
      providers: [
        { provide: LoginService, useValue: loginServiceSpy },
        { provide: SessionService, useValue: sessionServiceStub },
        { provide: FormHelperService, useValue: formHelperStub },
        { provide: NotificationService, useValue: notificationSpy }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    componentDe = fixture.debugElement;
    component.loginForm.controls['rememberMe'].setValue(false);
    fixture.detectChanges();
    navigateByUrlSpy = spyOn((<any>component).router, 'navigateByUrl');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call the sessionService#login and navigate to the `/home` route when login action is performed',
    fakeAsync(() => {
      setForm();
      componentDe.query(By.css('form')).triggerEventHandler('submit', null);
      fixture.detectChanges();

      expect(component.loginForm.valid).toBeTruthy('the login form should be valid');
      expect(loginServiceSpy.login).toHaveBeenCalledTimes(1);
      subjectLoginResponseModel.next(sampleRes);
      tick();
      // observable arrived from login service
      expect(navigateByUrlSpy).toHaveBeenCalledTimes(1);
      expect(navigateByUrlSpy.calls.first().args[0]).toEqual('home',
        'should navigate to `home` once user is logged in');
    }));

  it('should mark all controls as untouched under the login form if it is invalid', () => {
    componentDe.query(By.css('form')).triggerEventHandler('submit', null);
    fixture.detectChanges();

    expect(component.loginForm.invalid).toBeTruthy('the login form should be invalid');
    expect(loginServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should navigate to `home` if sessionService#isLoggedIn returned observable brings `true`',
    fakeAsync(() => {
      sessionServiceIsLoggedInSubject.next(true);
      tick();
      expect(navigateByUrlSpy).toHaveBeenCalledTimes(1);
    }));

  it('should not navigate to `home` if sessionService#isLoggedIn resolved to an error (i.e.: 401 error)',
    fakeAsync(() => {
      setForm();
      // change the fake observable to return an error instead of a success response
      const errorObservable: Observable<LoginResponseModel> = new Observable(observer => {
        observer.error(new HttpErrorResponse({ status: HttpStatusCode.UNAUTHORIZED, error: 'test error' }));
        observer.complete();
      });
      loginServiceSpy.login.and.returnValue(errorObservable);

      componentDe.query(By.css('form')).triggerEventHandler('submit', null);
      fixture.detectChanges();
      expect(loginServiceSpy.login).toHaveBeenCalledTimes(1);
      tick();
      expect(navigateByUrlSpy).not.toHaveBeenCalled(); // login error, code for error handling executed
      expect(notificationSpy.error).toHaveBeenCalled();
    }));

  /**
   * Sets some sample values in the login form
   */
  function setForm() {
    component.loginForm.controls['usernameOrEmail'].setValue(sampleReq.usernameOrEmail);
    component.loginForm.controls['password'].setValue(sampleReq.password);
  }
});
