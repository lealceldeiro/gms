import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { SessionService } from './core/session/session.service';
import { of } from 'rxjs/index';

import { AppComponent } from './app.component';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  // region mocks
  @Component({selector: 'gms-nav-bar', template: ''})
  class NavBarStubComponent {}

  @Component({selector: 'gms-side-menu', template: ''})
  class SideMenuStubComponent {}

  @Component({selector: 'router-outlet', template: ''})  // tslint:disable-line
  class RouterOutletStubComponent {}

  const sessionServiceStub = {
    isNotLoggedIn: () => of(false),
    isLoggedIn: () => of(true),
  };
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent, NavBarStubComponent, SideMenuStubComponent, RouterOutletStubComponent ],
      providers: [ { provide: SessionService, useValue: sessionServiceStub}]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', async(() => {
    expect(component).toBeTruthy();
  }));
});
