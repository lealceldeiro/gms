import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';
import { SessionService } from './core/session/session.service';
import { SessionStubService } from './shared/mock/session-stub.service';

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

  const sessionStubService: Partial<SessionStubService> = new SessionStubService();
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent, NavBarStubComponent, SideMenuStubComponent, RouterOutletStubComponent ],
      providers: [ { provide: SessionService, useValue: sessionStubService}]
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
