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

    const a = componentEl.querySelector('p a');

    expect(componentEl.querySelector('h2').textContent.trim()).toEqual(sampleHeading.trim(), 'did not bind properly `heading`');
    expect(componentEl.querySelector('p').textContent.trim()).toEqual(sampleText.trim(), 'did not bind properly `text`');
    expect(a.getAttribute('href')).toEqual(sampleLink, 'did not bind properly `link`');
    expect(a.textContent.trim()).toEqual(sampleLinkText.trim(), 'did not bind properly `linkText`');
  });
});
