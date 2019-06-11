import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { getRandomNumber } from '../../test-util/functions.util';
import { GmsPaginationComponent } from './gms-pagination.component';
import { DebugElement, Component, Input, Output, EventEmitter } from '@angular/core';


describe('GmsPaginationComponent', () => {

  @Component({ template: ``, selector: 'ngb-pagination' })
  class DummyNgPagination {
    @Input() boundaryLinks: boolean = false;
    @Input() collectionSize: number = 0;
    @Input() directionLinks: boolean = true;
    @Input() disabled: boolean = false;
    @Input() ellipses: boolean = true;
    @Input() maxSize: number = 10;
    @Input() page: number = 1;
    @Input() pageSize: number = 10;
    @Input() rotate: boolean = false;
    @Input() size: string = '-';
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
    onPageChangeAction: (page: number) => { }
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

    spyOnEmit = spyOn(component.onPageChangeAction, 'emit');

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call EventEmitter(onPageChangeAction)#emit on click to a new page, different from the selected one', () => {
    let anotherPage = getRandomNumber();
    if (component['previous'] === anotherPage) {
      anotherPage++;
    }
    component.onPageChange(anotherPage);
    fixture.detectChanges();
    expect(spyOnEmit).toHaveBeenCalledTimes(1);
    expect(spyOnEmit).toHaveBeenCalledWith(anotherPage);
  });

  it('should not call EventEmitter(onPageChangeAction)#emit on click to the same page', () => {
    let anotherPage = getRandomNumber();
    component['previous'] = anotherPage;
    component.onPageChange(anotherPage);
    fixture.detectChanges();
    expect(spyOnEmit).not.toHaveBeenCalled();
  });
});
