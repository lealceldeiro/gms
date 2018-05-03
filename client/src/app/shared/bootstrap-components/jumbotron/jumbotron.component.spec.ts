import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GmsJumbotronComponent } from './jumbotron.component';
import { RouterLinkStubDirective } from '../../mock/router-link-stub.directive';

describe('GmsJumbotronComponent', () => {
  let component: GmsJumbotronComponent;
  let fixture: ComponentFixture<GmsJumbotronComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GmsJumbotronComponent, RouterLinkStubDirective ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GmsJumbotronComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });
});
