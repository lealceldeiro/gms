import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewContentComponent } from './preview-content.component';

describe('PreviewContentComponent', () => {
  let component: PreviewContentComponent;
  let fixture: ComponentFixture<PreviewContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreviewContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
