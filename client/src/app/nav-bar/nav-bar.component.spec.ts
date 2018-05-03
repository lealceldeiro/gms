import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBarComponent } from './nav-bar.component';
import { RouterLinkStubDirective } from '../shared/mock/router-link-stub.directive';
import { NgbCollapseStubDirective } from '../shared/mock/ngb-collapse-stub.directive';
import { Router } from '@angular/router';

describe('NavBarComponent', () => {
  let component: NavBarComponent;
  let fixture: ComponentFixture<NavBarComponent>;

  // region mocks
  const routerSpy = jasmine.createSpyObj('Router', ['isActive']);
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavBarComponent, RouterLinkStubDirective, NgbCollapseStubDirective ],
      providers: [ { provide: Router, useValue: routerSpy }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
