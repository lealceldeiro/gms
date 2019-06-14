import { Component, DebugElement } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PageNotFoundService } from '../core/navigation/page-not-found.service';
import { PageNotFoundComponent } from './page-not-found.component';

describe('PageNotFoundComponent', () => {
  let component: PageNotFoundComponent;
  let fixture: ComponentFixture<PageNotFoundComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  @Component({ selector: 'gms-dummy', template: '' })
  class DummyComponent { }

  let wasNotFoundFn: { (): boolean; (): boolean; (): void; };
  let spyWasNotFound: jasmine.Spy;
  let spyAddUrl: jasmine.Spy;
  const spies = { wasNotFound: (_a: any) => { }, addUrl: (_a: any) => { } };

  const pageNotFoundServiceStub = {
    wasNotFound: (a: any) => { spies.wasNotFound(a); return wasNotFoundFn(); },
    addUrl: (a: any) => { spies.addUrl(a); }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'test', component: DummyComponent }])],
      declarations: [PageNotFoundComponent, DummyComponent],
      providers: [{ provide: PageNotFoundService, useValue: pageNotFoundServiceStub }]
    }).compileComponents();
  }));

  beforeEach(() => {
    wasNotFoundFn = () => true;
    spyWasNotFound = spyOn(spies, 'wasNotFound');
    spyAddUrl = spyOn(spies, 'addUrl');
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create with default values', () => {
    const divMsg = componentEl.querySelector('div.msg');
    expect(divMsg).not.toBeNull();
    if (divMsg) {
      expect(divMsg.textContent).toContain('Sorry!', 'should render `Sorry!` in a div');
    }

    const h2 = componentEl.querySelector('h2');
    expect(h2).not.toBeNull();
    if (h2) {
      expect(h2.textContent).toBe('Page not found', 'should render `Page not found` in an <h2>');
    }
  });

  it('should call PageNotFoundService#wasNotFound on init', () => {
    expect(spyWasNotFound).toHaveBeenCalledTimes(1);
  });

  it('should call PageNotFoundService#addUrl if PageNotFoundService#wasNotFound returns false on init', () => {
    // re-configure whole test for this scenario when wasNotFoundFn returns false
    wasNotFoundFn = () => false;
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
    expect(spyAddUrl).toHaveBeenCalledTimes(1);
  });

  it('should not call PageNotFoundService#addUrl if PageNotFoundService#wasNotFound returns true on init', () => {
    expect(spyAddUrl).not.toHaveBeenCalled();
  });
});
