import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { getRandomNumber } from '../../test-util/functions.util';
import { GmsPaginationComponent } from './gms-pagination.component';
import { DebugElement, Component, Input, Output, EventEmitter } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('GmsPaginationComponent', () => {

  // tslint:disable-next-line:component-selector
  @Component({ template: ``, selector: 'ngb-pagination' })
  class DummyNgPagination {
    @Input() boundaryLinks = false;
    @Input() collectionSize = 0;
    @Input() directionLinks = true;
    @Input() disabled = false;
    @Input() ellipses = true;
    @Input() maxSize = 10;
    @Input() page = 1;
    @Input() pageSize = 10;
    @Input() rotate = false;
    @Input() size = '-';
  }

  let component: GmsPaginationComponent;
  let fixture: ComponentFixture<GmsPaginationComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;
  let spyOnEmit: jasmine.Spy;

  // some arbitrary values to generate random numbers and booleans in order
  // to make tests more realistic
  const min = 1, max = 11, pivot = 6;

  const dummy = {
    boundaryLinks: getRandomNumber(min, max) > pivot,
    collectionSize: getRandomNumber(1, 25),
    directionLinks: getRandomNumber(min, max) > pivot,
    disabled: getRandomNumber(1, 25) > pivot,
    ellipses: getRandomNumber(min, max) > pivot,
    maxSize: getRandomNumber(1, 25),
    page: getRandomNumber(1, 3),
    pageSize: getRandomNumber(min, max),
    rotate: getRandomNumber(min, max) > pivot,
    size: '-',
    pageChangeAction: (page: number) => { }
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GmsPaginationComponent, DummyNgPagination]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GmsPaginationComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;

    component.boundaryLinks = dummy.boundaryLinks;
    component.collectionSize = dummy.collectionSize;
    component.directionLinks = dummy.directionLinks;
    component.disabled = dummy.disabled;
    component.ellipses = dummy.ellipses;
    component.maxSize = dummy.maxSize;
    component.pageSize = dummy.pageSize;
    component.rotate = dummy.rotate;
    component.size = dummy.size;

    spyOnEmit = spyOn(component.pageChangeAction, 'emit');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call EventEmitter(pageChangeAction)#emit on click to a new page, different from the selected one', () => {
    let anotherPage = getRandomNumber();
    if (component['previous'] === anotherPage) { // make sure that the random generated for anotherPage doesn't collide with the previous
      anotherPage++;
    }
    component.onPageChange(anotherPage);
    fixture.detectChanges();
    expect(spyOnEmit).toHaveBeenCalledTimes(1);
    expect(spyOnEmit).toHaveBeenCalledWith(anotherPage);
  });

  it('should not call EventEmitter(pageChangeAction)#emit on click to the same page', () => {
    const anotherPage = getRandomNumber();
    component['previous'] = anotherPage;
    component.onPageChange(anotherPage);
    fixture.detectChanges();
    expect(spyOnEmit).not.toHaveBeenCalled();
  });

  it('should not render if pageSize value is <= 0', () => {
    component.pageSize = 0;
    fixture.detectChanges();
    expect(componentDe.query(By.css('#gms-pagination'))).toBeFalsy();
  });

  it('should not render if condition (collectionSize / pageSize) > 1 does not evaluates to true', () => {
    component.collectionSize = 5;
    component.pageSize = 5;
    fixture.detectChanges();
    expect(componentDe.query(By.css('#gms-pagination'))).toBeFalsy();
  });

  it('should render if condition (collectionSize / pageSize) > 1 evaluates to true', () => {
    // two pages
    component.collectionSize = 10;
    component.pageSize = 5;
    fixture.detectChanges();
    expect(componentDe.query(By.css('#gms-pagination'))).toBeFalsy();
  });
});
