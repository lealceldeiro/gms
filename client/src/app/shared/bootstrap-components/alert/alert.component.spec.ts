import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { getRandomNumber } from '../../test-util/functions.util';
import { AlertComponent } from './alert.component';

describe('AlertComponent', () => {
  let component: AlertComponent;
  let fixture: ComponentFixture<AlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AlertComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
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
    const emitSpy = spyOn(component.closeAlert, 'emit');
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
    component.ngOnInit();
    expect(setTimeoutSpy).toHaveBeenCalledTimes(1);
    expect(setTimeoutSpy.calls.first().args[1]).toEqual(milliseconds);
  });
});
