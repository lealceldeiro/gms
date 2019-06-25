import { BooleanPipe } from './boolean.pipe';
import { getRandomNumber } from '../test-util/functions.util';

describe('BooleanPipe', () => {

  it('create an instance', () => {
    const pipe = new BooleanPipe();
    expect(pipe).toBeTruthy();
  });

  it('should return true when no value is specified as first argument - true case', () => {
    const pipe = new BooleanPipe();
    expect(pipe.transform(true)).toEqual('true');
  });

  it('should return false when no value is specified as first argument - false case', () => {
    const pipe = new BooleanPipe();
    expect(pipe.transform(false)).toEqual('false');
  });

  it('should return a custom value when specified as first argument - true case', () => {
    const pipe = new BooleanPipe();
    const trueVal = 'val-' + getRandomNumber() + 'rnd';
    expect(pipe.transform(true, trueVal)).toEqual(trueVal);
  });

  it('should return a custom value when specified as second argument - false case', () => {
    const pipe = new BooleanPipe();
    const falsyVal = 'val-' + getRandomNumber() + 'rnd';
    expect(pipe.transform(false, undefined, falsyVal)).toEqual(falsyVal);
  });

});
