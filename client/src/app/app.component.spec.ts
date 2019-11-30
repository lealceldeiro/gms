import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { of, Subject } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './core/session/session.service';

describe('AppComponent', () => {
  const event = new Event('beforeunload', { cancelable: true });
  const subject = new Subject<boolean>();
  let sessionServiceSpy: jasmine.SpyObj<SessionService>;
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async(() => {
    sessionServiceSpy = jasmine.createSpyObj(
      'SessionService',
      ['loadInitialData', 'closeSession', 'isRememberMe', 'isLoggedIn', 'loadInitialData']
    );
    sessionServiceSpy.isRememberMe.and.returnValue(subject.asObservable());
    sessionServiceSpy.isLoggedIn.and.returnValue(of(true));

    TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [{ provide: SessionService, useValue: sessionServiceSpy }],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should call SessionService#loadInitialData on init', () => {
    component.ngOnInit();
    expect(sessionServiceSpy.loadInitialData).toHaveBeenCalled();
  });

  it(`should call SessionService#isRememberMe in order to get to know whether the session data should be kept
    or not ("remember me" is true)`, () => {
    window.dispatchEvent(event);
    expect(sessionServiceSpy.isRememberMe).toHaveBeenCalled();
  });

  it(`should call SessionService#isRememberMe in order to get to know whether the session data should be kept
    or not ("remember me" is false)`, () => {
    window.dispatchEvent(event);
    expect(sessionServiceSpy.isRememberMe).toHaveBeenCalledTimes(1);
    subject.next(false); // do not remember
    expect(sessionServiceSpy.closeSession).toHaveBeenCalled();
  });
});
