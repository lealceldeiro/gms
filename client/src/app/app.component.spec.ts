import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';

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
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent, NavBarStubComponent, SideMenuStubComponent, RouterOutletStubComponent ],
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
