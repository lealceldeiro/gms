import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, Input } from '@angular/core';
import { of, Subject } from 'rxjs/index';

import { SessionService } from './core/session/session.service';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  const event = new Event('beforeunload', { cancelable: true });
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let isRememberMeSpy: jasmine.Spy;
  let closeSessionSpy: jasmine.Spy;

  // region mocks
  @Component({selector: 'gms-nav-bar', template: ''})
  class NavBarStubComponent {}

  @Component({selector: 'gms-side-menu', template: ''})
  class SideMenuStubComponent {}

  @Component({selector: 'router-outlet', template: ''})  // tslint:disable-line
  class RouterOutletStubComponent {}

  @Component({selector: 'spinner', template: ''})  // tslint:disable-line
  class SpinnerStubComponent { @Input() spinner: any; }

  const spy = { isRememberMe: () => {}, closeSession: () => {} };
  const subject = new Subject();

  const sessionServiceStub = {
    isNotLoggedIn: () => of(false),
    isLoggedIn: () => of(true),
    isRememberMe: () => { spy.isRememberMe(); return subject.asObservable(); },
    closeSession: () => { spy.closeSession(); }
  };
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent, NavBarStubComponent, SideMenuStubComponent, RouterOutletStubComponent,
        SpinnerStubComponent ],
      providers: [ { provide: SessionService, useValue: sessionServiceStub}]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    isRememberMeSpy = spyOn(spy, 'isRememberMe');
    closeSessionSpy = spyOn(spy, 'closeSession');
    fixture.detectChanges();
  });

  it('should create the app', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should call SessionService#isRememberMe in order to get to know whether the session data should be kept ' +
    'or not ("remember me" is true)', () => {
    window.dispatchEvent(event);
    expect(isRememberMeSpy).toHaveBeenCalled();
  });

  it('should call SessionService#isRememberMe in order to get to know whether the session data should be kept ' +
    'or not ("remember me" is false)', () => {
    window.dispatchEvent(event);
    expect(isRememberMeSpy).toHaveBeenCalledTimes(1);
    subject.next(false); // do not delete data
    expect(closeSessionSpy).toHaveBeenCalled();
  });
});
