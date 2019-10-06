import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BehaviorSubject, Subscription } from 'rxjs';

import { SessionService } from '../core/session/session.service';
import { HomeComponent } from './home.component';
import { AppConfig } from '../core/config/app.config';
import { MockAppConfig } from '../shared/test-util/mock/app.config';

describe('HomeComponent', () => {
  const behaviorSubject = new BehaviorSubject<boolean>(false);
  let component: HomeComponent;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<HomeComponent>;
  let subscription: Subscription;
  let sessionServiceSpy: jasmine.SpyObj<SessionService>;

  beforeEach(async(() => {
    sessionServiceSpy = jasmine.createSpyObj('SessionService', ['isLoggedIn']);
    sessionServiceSpy.isLoggedIn.and.returnValue(behaviorSubject.asObservable());

    TestBed.configureTestingModule({
      declarations: [HomeComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: SessionService, useValue: sessionServiceSpy }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    AppConfig.settings = MockAppConfig.settings;
    behaviorSubject.next(false);
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    fixture.detectChanges();
  });

  afterEach(() => {
    if (subscription) {
      subscription.unsubscribe();
    }
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should call SessionService#isLoggedIn on init', () => {
    component.ngOnInit();
    expect(sessionServiceSpy.isLoggedIn).toHaveBeenCalled();
  });

  it('should not render jumbotron if the user is logged in', () => {
    subscription = sessionServiceSpy.isLoggedIn().subscribe((li) => {
      if (li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).toBeNull();
      }
    });
    behaviorSubject.next(true);
  });

  it('should render jumbotron if the user is not logged in', () => {
    subscription = sessionServiceSpy.isLoggedIn().subscribe((li) => {
      if (!li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).not.toBeNull('Jumbotron panel should not be null');
      }
    });
    behaviorSubject.next(false);
  });
});
