import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';

import { PageNotFoundComponent } from './page-not-found.component';

describe('PageNotFoundComponent', () => {
  let component: PageNotFoundComponent;
  let fixture: ComponentFixture<PageNotFoundComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PageNotFoundComponent ]
    })
      .compileComponents();
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

  it('should create with default values', () => {
    fixture.whenRenderingDone().then(() => {
      expect(componentEl.querySelector('span.firstDigit').textContent).toBe('4');
      expect(componentEl.querySelector('span.secondDigit').textContent).toBe('0');
      expect(componentEl.querySelector('span.thirdDigit').textContent).toBe('4');
    });
  });

  it('should render `Sorry!` in a div', () => {
    expect(componentEl.querySelector('div.msg').textContent).toContain('Sorry!');
  });

  it('should render `Page not found` in an <h2>', () => {
    expect(componentEl.querySelector('h2').textContent).toBe('Page not found');
  });
});
