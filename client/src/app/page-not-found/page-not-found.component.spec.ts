import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, DebugElement } from '@angular/core';

import { PageNotFoundComponent } from './page-not-found.component';
import { RouterTestingModule } from '@angular/router/testing';
import { PageNotFoundService } from '../core/navigation/page-not-found.service';

describe('PageNotFoundComponent', () => {
  let component: PageNotFoundComponent;
  let fixture: ComponentFixture<PageNotFoundComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;
  let c = 0;

  @Component({selector: 'gms-dummy', template: ''})
  class DummyComponent {}

  const pageNotFoundServiceStub = { wasNotFound: () => true, addUrl: () => {} };

  beforeEach(async(() => {
    pageNotFoundServiceStub.wasNotFound = (): boolean => c++ % 2 === 0; // alternate btween true and false
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'test', component: DummyComponent }])],
      declarations: [ PageNotFoundComponent, DummyComponent ],
      providers: [ { provide: PageNotFoundService, useValue: pageNotFoundServiceStub }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create with default values if the router url is ' + ((c - 1 % 2 !== 0) ? 'NOT' : '') +
    'added to the collection of 404 in pageNotFoundService', () => {
    checkItWasRendered();
  });

  it('should create with default values if the router url is ' + ((c - 1 % 2 !== 0) ? 'NOT' : '') +
    'added to the collection of 404 in pageNotFoundService', () => {
    checkItWasRendered();
  });

  function checkItWasRendered(): void {
    fixture.whenRenderingDone().then(() => {
      expect(componentEl.querySelector('span.firstDigit').textContent).toBe('4');
      expect(componentEl.querySelector('span.secondDigit').textContent).toBe('0');
      expect(componentEl.querySelector('span.thirdDigit').textContent).toBe('4');

      expect(componentEl.querySelector('div.msg').textContent).toContain('Sorry!',
        'should render `Sorry!` in a div');

      expect(componentEl.querySelector('h2').textContent).toBe('Page not found',
        'should render `Page not found` in an <h2>');
    });
  }
});
