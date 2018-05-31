import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/index';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { NavBarComponent } from './nav-bar.component';
import { MockModule } from '../shared/mock/mock.module';
import { DummyStubComponent } from '../shared/mock/dummy-stub.component';
import { gmsClick } from '../shared/test-util/mouse.util';
import { SessionService } from '../core/session/session.service';
import { userMock } from '../core/session/user.mock.model';
import Spy = jasmine.Spy;

describe('NavBarComponent', () => {
  let component: NavBarComponent;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;
  let fixture: ComponentFixture<NavBarComponent>;
  let spyIsActive: Spy;
  let spyCloseSession: Spy;

  // region mocks
  const sessionServiceStub = {
    isNotLoggedIn: () => of(true),
    isLoggedIn: () => of(false),
    getUser: () => of(userMock),
    closeSession: () => { }
  };

  const routes = [
    { path: 'help', component: DummyStubComponent },
    { path: 'about', component: DummyStubComponent },
    { path: 'home', component: DummyStubComponent }
  ];
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavBarComponent ],
      imports: [ MockModule, RouterTestingModule.withRoutes(routes), NgbModule.forRoot() ],
      providers: [ { provide: SessionService, useValue: sessionServiceStub } ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavBarComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
    spyIsActive = spyOn((<any>component).router, 'isActive');
    spyCloseSession = spyOn((<any>component).sessionService, 'closeSession');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('every link should be marked as active when the component navigates to the route', () => {
    const urls = component.urls;
    const leftLinks: NodeListOf<Element> = componentEl.querySelectorAll('ul#nav-left-list li a');
    for (let i = leftLinks.length - 1; i >= 0; i--) {
      gmsClick(leftLinks.item(i) as HTMLElement);
      fixture.detectChanges();
      expect(spyIsActive.calls.all()[i].args[0]).toEqual(urls[i].path);
    }
  });

  it('every link should have the route and name specified in the urls property', () => {
    const urls = component.urls;

    const links: NodeListOf<Element> = componentEl.querySelectorAll('ul#nav-left-list li a');
    for (let i = links.length - 1; i >= 0; i--) {
      expect(links.item(i).getAttribute('href')).toEqual(urls[i].path);
      expect(links.item(i).textContent).toContain(urls[i].name);
    }
  });

  it('should show the search form only when `isSearchActive` is `true`', () => {
    expect(getSearchForm()).toBeTruthy('form is not created by default');

    component.isSearchActive = false;
    fixture.detectChanges();

    expect(getSearchForm()).toBeFalsy('form is created when `isSearchActive` is `false`');
  });

  it('should bind properly the input values', () => {
    const samplePlaceHolder = 'sample placeholder';
    const sampleSearchText = 'sample text';

    component.searchPlaceholder = samplePlaceHolder;
    component.searchText = sampleSearchText;

    fixture.detectChanges();

    expect(getSearchInput().getAttribute('placeholder')).toEqual(samplePlaceHolder, '`placeholder` is incorrect');
    expect(getSearchInput().getAttribute('aria-label')).toEqual(samplePlaceHolder, '`aria-label` is incorrect');
    expect(componentEl.querySelector('form[name=nav-bar-search-form] div div button[type=button]').textContent.trim())
      .toEqual(sampleSearchText.trim(), '`searchText` is incorrect');
  });

  it('should call sessionService#closeSession when click on `logout` link', () => {
    component.loggedIn = true;
    fixture.detectChanges();
    gmsClick(componentEl.querySelector<HTMLElement>('#sign-out'));
    fixture.detectChanges();
    expect(spyCloseSession).toHaveBeenCalled();
  });

  function getSearchForm(): HTMLElement | any {
    return componentEl.querySelector('form[name=nav-bar-search-form]');
  }

  function getSearchInput(): HTMLElement | any {
    return componentEl.querySelector('form[name=nav-bar-search-form] div input[type=search]');
  }
});
