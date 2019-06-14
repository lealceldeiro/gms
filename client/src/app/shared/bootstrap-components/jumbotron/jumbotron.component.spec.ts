import { DebugElement } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { GmsJumbotronComponent } from './jumbotron.component';

describe('GmsJumbotronComponent', () => {
  let component: GmsJumbotronComponent;
  let fixture: ComponentFixture<GmsJumbotronComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GmsJumbotronComponent],
      imports: [RouterTestingModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GmsJumbotronComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create with default values', () => {
    expect(component.header).toEqual('');
    expect(component.text).toEqual('');
    expect(component.link).toEqual('');
    expect(component.linkText).toEqual('');
    expect(component.sizeClass).toEqual('btn-lg');
    expect(component.typeClass).toEqual('btn-primary');
  });

  it('should not create html elements which are not defined', () => {
    component.header = '';
    component.text = '';
    expect(componentEl.querySelector('h2')).toBeNull();
    expect(componentEl.querySelector('p#id-jumbotron-text')).toBeNull();
  });

  it('should bind properly the input values', () => {
    const sampleHeader = 'sample heading';
    const sampleText = 'sample text';
    const sampleLink = '/sample/link';
    const sampleLinkText = 'sample link text';
    const sampleSizeClass = 'sample-size-class';
    const sampleTypeClass = 'sample-type-class';

    component.header = sampleHeader;
    component.text = sampleText;
    component.link = sampleLink;
    component.linkText = sampleLinkText;
    component.sizeClass = sampleSizeClass;
    component.typeClass = sampleTypeClass;

    fixture.detectChanges();

    const h2 = componentEl.querySelector('h2');
    expect(h2).not.toBeNull();
    if (h2) {
      expect(h2.textContent).not.toBeNull();
      if (h2.textContent) {
        expect(h2.textContent.trim()).toEqual(sampleHeader.trim(), 'did not bind properly `header`');
      }
    }

    const jumbotrom = componentEl.querySelector('p#id-jumbotron-text');
    if (jumbotrom) {
      expect(jumbotrom.textContent).not.toBeNull();
      if (jumbotrom.textContent) {
        expect(jumbotrom.textContent.trim()).toEqual(sampleText.trim(), 'did not bind properly `text`');
      }
    }

    const a = componentEl.querySelector('p a');
    expect(a).not.toBeNull();
    if (a) {
      expect(a.getAttribute('href')).toEqual(sampleLink, 'did not bind properly `link`');
      expect(a.textContent).not.toBeNull();
      if (a.textContent) {
        expect(a.textContent.trim()).toEqual(sampleLinkText.trim(), 'did not bind properly `linkText`');
      }
      expect(a.getAttribute('class')).not.toBeNull();
      if (a.getAttribute('class')) {
        expect(a.getAttribute('class')).toContain(sampleTypeClass, 'did not bind properly `typeClass`');
        expect(a.getAttribute('class')).toContain(sampleSizeClass, 'did not bind properly `sizeClass`');
      }
    }
  });
});
