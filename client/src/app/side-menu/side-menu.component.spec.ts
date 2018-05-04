import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SideMenuComponent } from './side-menu.component';
import { RouterLinkStubDirective } from '../shared/mock/router-link-stub.directive';
import { NgbCollapseStubDirective } from '../shared/mock/ngb-collapse-stub.directive';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { gmsClick } from '../shared/mock/util';

describe('SideMenuComponent', () => {
  let component: SideMenuComponent;
  let fixture: ComponentFixture<SideMenuComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;
  let spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SideMenuComponent, RouterLinkStubDirective, NgbCollapseStubDirective ],
      imports: [ RouterTestingModule ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SideMenuComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = componentDe.nativeElement;

    fixture.detectChanges();
    spy = spyOn((<any>component).router, 'isActive');
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('every link should be marked as active when the component navigates to the route', () => {
    const urls = component.urls;
    const links: NodeListOf<Element> = componentEl.querySelectorAll('div nav div ul li a ');
    for (let i = links.length - 1; i >= 0; i--) {
      gmsClick(links.item(i) as HTMLElement);
      fixture.detectChanges();
      expect(spy.calls.all()[i].args[0]).toEqual(urls[i].path);
    }
  });

  it('every link should have the route and name specified in the urls property', () => {
    const urls = component.urls;

    const links: NodeListOf<Element> = componentEl.querySelectorAll('div nav div ul li a ');
    for (let i = links.length - 1; i >= 0; i--) {
      expect(links.item(i).getAttribute('href')).toEqual(urls[i].path);
      expect(links.item(i).textContent).toContain(urls[i].name);
    }
  });
});
