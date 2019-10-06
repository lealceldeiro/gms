import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { of, Subject } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './core/session/session.service';

describe('AppComponent', () => {
  const event = new Event('beforeunload', { cancelable: true });

  let isRememberMeSpy: jasmine.Spy;
  let closeSessionSpy: jasmine.Spy;

  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  // region mocks
  @Component({ selector: 'gms-nav-bar', template: '' })
  class NavBarStubComponent { }

  @Component({ selector: 'gms-side-menu', template: '' })
  class SideMenuStubComponent { }

  @Component({ selector: 'router-outlet', template: '' })  // tslint:disable-line
  class RouterOutletStubComponent { }

  @Component({ selector: 'ngx-ui-loader', template: '' })  // tslint:disable-line
  class LoadingIndicatorComponent { }

  const spy = { isRememberMe: () => { }, closeSession: () => { } };
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
      declarations: [AppComponent, NavBarStubComponent, SideMenuStubComponent, RouterOutletStubComponent,
        LoadingIndicatorComponent],
      providers: [{ provide: SessionService, useValue: sessionServiceStub }]
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
