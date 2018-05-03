import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewContentComponent } from './preview-content.component';
import { RouterLinkStubDirective } from '../../mock/router-link-stub.directive';

describe('PreviewContentComponent', () => {
  let component: PreviewContentComponent;
  let fixture: ComponentFixture<PreviewContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreviewContentComponent, RouterLinkStubDirective ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });
});
