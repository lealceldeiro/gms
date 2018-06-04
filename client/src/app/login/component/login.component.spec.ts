import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { Subject } from 'rxjs/internal/Subject';
import { RouterTestingModule } from '@angular/router/testing';

import { LoginComponent } from './login.component';
import { SharedModule } from '../../shared/shared.module';
import { LoginService } from '../service/login.service';
import { MockModule } from '../../shared/mock/mock.module';
import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { DummyStubComponent } from '../../shared/mock/dummy-stub.component';
import { SessionService } from '../../core/session/session.service';
import { Observable } from 'rxjs/index';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let componentEl: HTMLElement;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<LoginComponent>;
  let loginSpy: jasmine.Spy;
  let navigateByUrlSpy: jasmine.Spy;

  const routes = [ { path : 'home', component: DummyStubComponent }];
  const text = 'sampleText';
  const sampleReq: LoginRequestModel = { password: text, usernameOrEmail: text };
  const sampleRes: LoginResponseModel = { username: 'testUser', token_type: 'testToken' };
  const subjectLRM = new Subject<LoginResponseModel>();
  let ret = (a) => subjectLRM.asObservable();
  const spy = { login: (a) => {} };
  const loginServiceStub = { login: (a) => { spy.login(a); return ret(a); } };
  const s = new Subject<boolean>();
  const sessionServiceStub = { isLoggedIn: () =>  s.asObservable(), setRememberMe: () => {} };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports: [ MockModule, SharedModule, RouterTestingModule.withRoutes(routes) ],
      providers: [
        { provide: LoginService, useValue: loginServiceStub },
        { provide: SessionService, useValue: sessionServiceStub }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    componentDe = fixture.debugElement;
    component.rememberMe = false;
    s.next(false);
    fixture.detectChanges();
    loginSpy = spyOn(spy, 'login');
    navigateByUrlSpy = spyOn((<any>component).router, 'navigateByUrl');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call the sessionService#loging and navigate to the `/home` route when login action is performed',
    fakeAsync(() => {
      component.usernameOrEmail = sampleReq.usernameOrEmail;
      component.password = sampleReq.password;
      componentDe.query(By.css('form')).triggerEventHandler('submit', null);
      fixture.detectChanges();

      expect(loginSpy).toHaveBeenCalledTimes(1);
      subjectLRM.next(sampleRes);
      tick();
      // observable arrived from login service
      expect(navigateByUrlSpy).toHaveBeenCalledTimes(1);
      expect(navigateByUrlSpy.calls.first().args[0]).toEqual('home',
        'should navigate to `home` once user is logged in');
    }));

  it('should navigate to `home` if sessionService#isLoggedIn returned observable brings `true`',
    fakeAsync(() => {
      s.next(true);
      tick();
      expect(navigateByUrlSpy).toHaveBeenCalledTimes(1);
    }));

  it('should not navigate to `home` if sessionService#isLoggedIn resolved to an error (i.e.: 401 error)',
    fakeAsync(() => {
      // change the fake observable to return an error instead of a success response
      ret = () => Observable.create(observer => { observer.error(new Error('test error')); observer.complete(); });

      componentDe.query(By.css('form')).triggerEventHandler('submit', null);
      fixture.detectChanges();
      expect(loginSpy).toHaveBeenCalledTimes(1);
      tick();
      expect(navigateByUrlSpy).not.toHaveBeenCalled(); // login error, code for error handling executed
    }));
});
