import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { getRandomNumber } from '../../test-util/functions.util';
import { PaginationComponent } from './pagination.component';

describe('GmsPaginationComponent', () => {
  // some arbitrary values to generate random numbers and booleans in order
  // to make tests more realistic
  const min = 1;
  const max = 11;
  const pivot = 6;
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
    pageChangeAction: () => { }
  };

  let component: PaginationComponent;
  let fixture: ComponentFixture<PaginationComponent>;
  let componentDe: DebugElement;
  let spyOnEmit: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PaginationComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginationComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;

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
    if (component['previous'] === anotherPage) {  // make sure that the random generated for anotherPage
      anotherPage++;                              // doesn't collide with the previous
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
    testRendering(false);
  });

  it('should render if condition (collectionSize / pageSize) > 1 evaluates to true', () => {
    testRendering();
  });

  const testRendering = (shouldRender = true) => {
    component.collectionSize = shouldRender ? getRandomNumber(10, 1000) : getRandomNumber(0, 5);
    component.pageSize = 5;
    fixture.detectChanges();

    const element: DebugElement = componentDe.query(By.css('#gms-pagination'));

    if (shouldRender) {
      expect(element).toBeTruthy();
    } else {
      expect(element).toBeFalsy();
    }
  };
});
