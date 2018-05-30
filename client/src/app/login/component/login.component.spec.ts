import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { Subject } from 'rxjs/internal/Subject';
import { RouterTestingModule } from '@angular/router/testing';

import { LoginComponent } from './login.component';
import { SharedModule } from '../../shared/shared.module';
import { LoginService } from '../service/login.service';
import { gmsClick } from '../../shared/test-util/mouse.util';
import { MockModule } from '../../shared/mock/mock.module';
import { LoginRequestModel } from '../../core/session/login-request.model';
import { LoginResponseModel } from '../../core/session/login-response.model';
import { DummyStubComponent } from '../../shared/mock/dummy-stub.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let componentEl: HTMLElement;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<LoginComponent>;
  let loginSpy: jasmine.Spy;
  let navigatedByUrlSpy: jasmine.Spy;

  const routes = [ { path : 'home', component: DummyStubComponent }];
  const text = 'sampleText';
  const sampleReq: LoginRequestModel = { password: text, usernameOrEmail: text };
  const sampleRes: LoginResponseModel = { username: 'testUser', token_type: 'testToken' };
  const subjectLRM = new Subject<LoginResponseModel>();
  const ret = (req: LoginRequestModel) => subjectLRM.asObservable();
  const spy = { login: (a) => {} };
  const loginServiceStub = { login: (a) => { spy.login(a); return ret(a); } };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports: [ MockModule, SharedModule, RouterTestingModule.withRoutes(routes) ],
      providers: [ { provide: LoginService, useValue: loginServiceStub }]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    componentDe = fixture.debugElement;
    fixture.detectChanges();
    loginSpy = spyOn(spy, 'login');
    navigatedByUrlSpy = spyOn((<any>component).router, 'navigateByUrl');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call the sessionService#loging and navigate to the `/home` route when login action is performed',
    fakeAsync(() => {
      component.usernameOrEmail = sampleReq.usernameOrEmail;
      component.password = sampleReq.password;

      const loginBtn = componentDe.query(By.css('button#signInBtn'));

      gmsClick(loginBtn);
      fixture.detectChanges();

      expect(loginSpy).toHaveBeenCalledTimes(1);
      subjectLRM.next(sampleRes);
      tick();
      // observable arrived from login service
      expect(navigatedByUrlSpy).toHaveBeenCalled();
      expect(navigatedByUrlSpy.calls.allArgs()[0][0]).toEqual('home',
        'should navigate to `home` once user is logged in');
    }));
});
