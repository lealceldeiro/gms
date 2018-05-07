import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { Component, Input } from '@angular/core';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  // region mocks
  @Component({selector: 'gms-jumbotron', template: ''})
  class JumbotronStubComponent { @Input() header: string; }

  @Component({selector: 'gms-preview-content', template: ''})
  class PreviewContentStubComponent {}
  // endregion

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeComponent, JumbotronStubComponent, PreviewContentStubComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
