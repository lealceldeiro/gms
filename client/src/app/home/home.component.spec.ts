import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BehaviorSubject } from 'rxjs';
import { SessionService } from '../core/session/session.service';
import { HomeComponent } from './home.component';


describe('HomeComponent', () => {
  let component: HomeComponent;
  let componentEl: HTMLElement;
  let componentDe: DebugElement;
  let fixture: ComponentFixture<HomeComponent>;
  const behaviorSubject = new BehaviorSubject<boolean>(false);

  const sessionServiceStub = {
    isLoggedIn: () => { spy.isLoggedInSpyFn(); return behaviorSubject.asObservable() }
  };
  const spy = { isLoggedInSpyFn: () => { } };
  let spyIsLoggedIn: jasmine.Spy;

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
    componentEl = fixture.nativeElement;
    componentDe = fixture.debugElement;
    fixture.detectChanges();
    spyIsLoggedIn = spyOn(spy, 'isLoggedInSpyFn');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call SessionService#isLoggedIn on init', () => {
    component.ngOnInit();
    expect(spyIsLoggedIn).toHaveBeenCalledTimes(1);
  });

  it('should not render jumbotron if the user is logged in', fakeAsync(() => {
    sessionServiceStub.isLoggedIn().subscribe((li) => {
      if (li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).toBeNull();
      }
    });
    behaviorSubject.next(true);
    tick();
  }));

  it('should render jumbotron if the user is not logged in', fakeAsync(() => {
    sessionServiceStub.isLoggedIn().subscribe((li) => {
      if (!li) {
        fixture.detectChanges();
        expect(componentDe.query(By.css('#jmb-panel'))).not.toBeNull();
      }
    });
    behaviorSubject.next(false);
    tick();
  }));
});
