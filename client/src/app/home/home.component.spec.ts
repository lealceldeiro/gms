import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BehaviorSubject, Subscription } from 'rxjs';
import { SessionService } from '../core/session/session.service';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<HomeComponent>;
  const behaviorSubject = new BehaviorSubject<boolean>(false);
  let subscription: Subscription;
  const spy = { isLoggedInSpyFn: () => { } };
  let spyIsLoggedIn: jasmine.Spy;

  const sessionServiceStub = {
    isLoggedIn: () => { spy.isLoggedInSpyFn(); return behaviorSubject.asObservable(); }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HomeComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [{ provide: SessionService, useValue: sessionServiceStub }]
    }).compileComponents();
  }));

  beforeEach(() => {
    behaviorSubject.next(false);
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    fixture.detectChanges();
    spyIsLoggedIn = spyOn(spy, 'isLoggedInSpyFn');
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
    expect(spyIsLoggedIn).toHaveBeenCalledTimes(1);
  });

  it('should not render jumbotron if the user is logged in', () => {
    subscription = sessionServiceStub.isLoggedIn().subscribe((li) => {
      if (li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).toBeNull();
      }
    });
    behaviorSubject.next(true);
  });

  it('should render jumbotron if the user is not logged in', () => {
    subscription = sessionServiceStub.isLoggedIn().subscribe((li) => {
      if (!li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).not.toBeNull('Jumbotron panel should not be null');
      }
    });
    behaviorSubject.next(false);
  });
});
