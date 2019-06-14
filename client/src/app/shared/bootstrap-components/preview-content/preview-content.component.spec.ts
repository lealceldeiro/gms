import { DebugElement } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PreviewContentComponent } from './preview-content.component';

describe('PreviewContentComponent', () => {
  let component: PreviewContentComponent;
  let fixture: ComponentFixture<PreviewContentComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PreviewContentComponent],
      imports: [RouterTestingModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewContentComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = componentDe.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create with default values', () => {
    expect(component.heading).toEqual('');
    expect(component.text).toEqual('');
    expect(component.link).toEqual('');
    expect(component.linkText).toEqual('View details »');

    const h2: Element = componentEl.querySelector('h2') as Element;
    const p: NodeListOf<Element> = componentEl.querySelectorAll('p');
    const a: NodeListOf<Element> = componentEl.querySelectorAll('a');

    expect(h2).toBeTruthy('there should be a header <h2>');
    expect(a).toBeTruthy('there should be a <a> for the link');
    expect(p).toBeTruthy('there should be created two <p>');
    expect(p.length).toEqual(2, 'there should be created two <p> (one for the `text` and another for the `link`');
  });

  it('should bind properly the input values', () => {
    const sampleHeading = 'sample heading';
    const sampleText = 'sample text';
    const sampleLink = '/sampleLink';
    const sampleLinkText = 'sample link text';

    component.heading = sampleHeading;
    component.text = sampleText;
    component.link = sampleLink;
    component.linkText = sampleLinkText;

    fixture.detectChanges();

    const h2 = componentEl.querySelector('h2');
    const p = componentEl.querySelector('p');
    const a = componentEl.querySelector('p a');

    expect(h2).not.toBeNull();
    if (h2) {
      expect(h2.textContent).not.toBeNull();
      if (h2.textContent) {
        expect(h2.textContent.trim()).toEqual(sampleHeading.trim(), 'did not bind properly `heading`');
      }
    }

    expect(a).not.toBeNull();
    if (a) {
      expect(a.getAttribute('href')).not.toBeNull();
      if (a.getAttribute('href')) {
        expect(a.getAttribute('href')).toEqual(sampleLink.trim(), 'did not bind properly `link`');
      }
    }

    expect(p).not.toBeNull();
    if (p) {
      expect(p.textContent).not.toBeNull();
      if (p.textContent) {
        expect(p.textContent.trim()).toEqual(sampleText.trim(), 'did not bind properly `text`');
      }
    }
  });
});
