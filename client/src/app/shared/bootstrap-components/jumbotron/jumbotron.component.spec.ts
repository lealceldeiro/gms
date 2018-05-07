import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GmsJumbotronComponent } from './jumbotron.component';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

describe('GmsJumbotronComponent', () => {
  let component: GmsJumbotronComponent;
  let fixture: ComponentFixture<GmsJumbotronComponent>;
  let componentDe: DebugElement;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GmsJumbotronComponent ],
      imports: [ RouterTestingModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GmsJumbotronComponent);
    component = fixture.componentInstance;
    componentDe = fixture.debugElement;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should create with default values', () => {
    expect(component.header).toBeUndefined();
    expect(component.text).toBeUndefined();
    expect(component.link).toBeUndefined();
    expect(component.linkText).toBeUndefined();
    expect(component.sizeClass).toEqual('btn-lg');
    expect(component.typeClass).toEqual('btn-primary');
  });

  it('should not create html elements which are not defined', () => {
    component.header = undefined;
    component.text = undefined;
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

    const a = componentEl.querySelector('p a');

    expect(componentEl.querySelector('h2').textContent.trim()).toEqual(sampleHeader.trim(), 'did not bind properly `header`');
    expect(componentEl.querySelector('p#id-jumbotron-text').textContent.trim()).toEqual(sampleText.trim(), 'did not bind properly `text`');
    expect(a.getAttribute('href')).toEqual(sampleLink, 'did not bind properly `link`');
    expect(a.textContent.trim()).toEqual(sampleLinkText.trim(), 'did not bind properly `linkText`');
    expect(a.getAttribute('class')).toContain(sampleTypeClass, 'did not bind properly `typeClass`');
    expect(a.getAttribute('class')).toContain(sampleSizeClass, 'did not bind properly `sizeClass`');
  });
});
