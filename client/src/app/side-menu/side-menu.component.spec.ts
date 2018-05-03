import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SideMenuComponent } from './side-menu.component';
import { Router } from '@angular/router';
import { RouterLinkStubDirective } from '../shared/mock/router-link-stub.directive';
import { NgbCollapseStubDirective } from '../shared/mock/ngb-collapse-stub.directive';

describe('SideMenuComponent', () => {
  let component: SideMenuComponent;
  let fixture: ComponentFixture<SideMenuComponent>;

  // region mocks
  const routerSpy = jasmine.createSpyObj('Router', ['isActive']);
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SideMenuComponent, RouterLinkStubDirective, NgbCollapseStubDirective ],
      providers: [ { provide: Router, useValue: routerSpy } ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SideMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
