import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { excluded } from '../app-routing.module';
import { DummyStubComponent } from '../shared/test-util/mock/dummy-stub.component';
import { MockModule } from '../shared/test-util/mock/mock.module';
import { getRandomNumber } from '../shared/test-util/functions.util';
import { gmsClick } from '../shared/test-util/mouse.util';
import { SideMenuComponent } from './side-menu.component';

import Spy = jasmine.Spy;

describe('SideMenuComponent', () => {
  let component: SideMenuComponent;
  let fixture: ComponentFixture<SideMenuComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;
  let spy: Spy;
  const numberOfRoutes = getRandomNumber(1, 11);
  const routes: Array<{ [key: string]: any }> = [];
  for (let i = 0; i < numberOfRoutes; i++) {
    routes.push({ path: 'dummy-' + i + '-' + getRandomNumber(), component: DummyStubComponent });
  }
  // add routes excluded in app-routing
  excluded.forEach(r => routes.push({ path: r, component: DummyStubComponent }));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SideMenuComponent],
      imports: [MockModule, RouterTestingModule.withRoutes(routes)],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SideMenuComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = componentDe.nativeElement;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('every link should be marked as active when the component navigates to the route', () => {
    spy = spyOn((<any>component).router, 'isActive');
    const urls = component.urls;
    const links: NodeListOf<Element> = componentEl.querySelectorAll('div nav div ul li a');
    for (let i = links.length - 1; i >= 0; i--) {
      gmsClick(links.item(i) as HTMLElement);
      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(urls[i].path, true);
    }
  });

  it('every link should have the route and name specified in the urls property', () => {
    const urls = component.urls;

    const links: NodeListOf<Element> = componentEl.querySelectorAll('div nav div ul li a');
    for (let i = links.length - 1; i >= 0; i--) {
      expect(links.item(i).getAttribute('href')).toEqual('/' + urls[i].path);
      expect(links.item(i).textContent).toContain(urls[i].name);
    }
  });

  it('#getNameFrom should return a logical name from a given url. ' +
    'i.e: from /business-management/local/ it should return Business Management Local', () => {
      expect(component['getNameFrom']('/business-management/local/')).toEqual('Business Management Local');
    });

  it('#getCapitalized should return a capitalized string. i.e: from `business` it returns `Business`', () => {
    expect(component['getCapitalized']('business')).toEqual('Business');
    expect(component['getCapitalized']('management')).toEqual('Management');
    expect(component['getCapitalized']('local')).toEqual('Local');
  });

  it('#getCleanUrls should return only the url that are good candidates to be shown in the UI ', () => {
    const randomVal = 'some-random-url' + getRandomNumber() + '-val';
    const testUrls = [
      '*sdf',         // nothing with asterisks
      'login',        // remove the login word url
      randomVal
    ];
    const expected = [randomVal];
    expect(component['getCleanUrls'](testUrls)).toEqual(expected);
  });

});
