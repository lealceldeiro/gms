import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewContentComponent } from './preview-content.component';
import { RouterLinkStubDirective } from '../../mock/router-link-stub.directive';
import { RouterTestingModule } from '@angular/router/testing';
import { DebugElement } from '@angular/core';

describe('PreviewContentComponent', () => {
  let component: PreviewContentComponent;
  let fixture: ComponentFixture<PreviewContentComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreviewContentComponent, RouterLinkStubDirective ],
      imports: [ RouterTestingModule ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewContentComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = componentDe.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should create with default values', () => {
    expect(component.heading).toBeUndefined();
    expect(component.text).toBeUndefined();
    expect(component.link).toBeUndefined();
    expect(component.linkText).toEqual('View details »');

    const h2: Element = componentEl.querySelector('h2');
    const p: NodeListOf<Element> = componentEl.querySelectorAll('p');
    const a: NodeListOf<Element> = componentEl.querySelectorAll('a');

    expect(h2).toBeTruthy('there should be a header <h2>');
    expect(a).toBeTruthy('there should be a <a> for the link');
    expect(p).toBeTruthy('there should be created two <p>');
    expect(p.length).toEqual(2, 'there should be created two <p> (one for the `text` and another for the `link`');
  });
});
