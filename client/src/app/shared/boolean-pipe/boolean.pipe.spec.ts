import { PipeTransform } from '@angular/core';

import { BooleanPipe } from './boolean.pipe';
import { getRandomNumber } from '../test-util/functions.util';

describe('BooleanPipe', () => {
  let pipe: PipeTransform;

  beforeEach(() => pipe = new BooleanPipe());

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return true when no value is specified as first argument - true case', () => {
    expect(pipe.transform(true)).toEqual('true');
  });

  it('should return false when no value is specified as first argument - false case', () => {
    expect(pipe.transform(false)).toEqual('false');
  });

  it('should return a custom value when specified as first argument - true case', () => {
    const trueVal = 'val-' + getRandomNumber() + 'rnd';
    expect(pipe.transform(true, trueVal)).toEqual(trueVal);
  });

  it('should return a custom value when specified as second argument - false case', () => {
    const falsyVal = 'val-' + getRandomNumber() + 'rnd';
    expect(pipe.transform(false, undefined, falsyVal)).toEqual(falsyVal);
  });

});
