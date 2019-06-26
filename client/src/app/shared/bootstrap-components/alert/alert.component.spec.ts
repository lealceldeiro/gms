import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { getRandomNumber } from '../../test-util/functions.util';
import { AlertComponent } from './alert.component';

describe('AlertComponent', () => {

  // tslint:disable-next-line:component-selector
  @Component({ selector: 'ngb-alert', template: `` })
  class DummyAlert {
    @Input() heading = '';
    @Input() message = '';
    @Input() dismissible = true;
    @Input() autoDismissible = false;
    @Input() dismissMilliseconds = 20000;
    @Input() type = 'info';
    @Input() closeMessage = '';
    @Output() close = new EventEmitter();
    shouldClosed = false;
  }

  let component: AlertComponent;
  let fixture: ComponentFixture<AlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AlertComponent, DummyAlert]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call EventEmitter#emit on call to #onClose with `closeMessage` as message', () => {
    const emitSpy = spyOn(component.close, 'emit');
    const emitMsg = 'someRandom-' + getRandomNumber() + '-Value';
    component.closeMessage = emitMsg;
    component.onClose();
    expect(emitSpy).toHaveBeenCalledTimes(1);
    expect(emitSpy).toHaveBeenCalledWith(emitMsg);
  });

  it('should set a timeout `ngOnInit` in order to auto-dismiss the alert if `this.autoDismissible` is true', () => {
    const setTimeoutSpy = spyOn(window, 'setTimeout');
    const milliseconds = getRandomNumber(1, 11111);
    component.autoDismissible = true;
    component.dismissMilliseconds = milliseconds;
    console.log(milliseconds);
    console.log(component.dismissMilliseconds);
    component.ngOnInit();
    expect(setTimeoutSpy).toHaveBeenCalledTimes(1);
    expect(setTimeoutSpy.calls.first().args[1]).toEqual(milliseconds);
  });
});
