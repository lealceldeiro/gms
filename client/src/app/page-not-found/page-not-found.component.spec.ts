import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { PageNotFoundService } from '../core/navigation/page-not-found.service';
import { PageNotFoundComponent } from './page-not-found.component';

describe('PageNotFoundComponent', () => {
  @Component({ selector: 'gms-dummy', template: '' })
  class DummyComponent { }

  let component: PageNotFoundComponent;
  let fixture: ComponentFixture<PageNotFoundComponent>;
  let componentEl: HTMLElement;

  let pageNotFoundServiceSpy: jasmine.SpyObj<PageNotFoundService>;

  beforeEach(async(() => {
    pageNotFoundServiceSpy = jasmine.createSpyObj('PageNotFoundService', ['wasNotFound', 'addUrl']);
    pageNotFoundServiceSpy.wasNotFound.and.returnValue(true);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'test', component: DummyComponent }])],
      declarations: [PageNotFoundComponent, DummyComponent],
      providers: [{ provide: PageNotFoundService, useValue: pageNotFoundServiceSpy }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
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
    expect(pageNotFoundServiceSpy.wasNotFound).toHaveBeenCalledTimes(1);
  });

  it('should call PageNotFoundService#addUrl if PageNotFoundService#wasNotFound returns false on init', () => {
    // re-configure whole test for this scenario when wasNotFoundFn returns false
    pageNotFoundServiceSpy.wasNotFound.and.returnValue(false);
    fixture = TestBed.createComponent(PageNotFoundComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
    expect(pageNotFoundServiceSpy.addUrl).toHaveBeenCalledTimes(1);
  });

  it('should not call PageNotFoundService#addUrl if PageNotFoundService#wasNotFound returns true on init', () => {
    expect(pageNotFoundServiceSpy.addUrl).not.toHaveBeenCalled();
  });
});
