import { BooleanPipe } from './boolean.pipe';

describe('BooleanPipe', () => {
  it('create an instance', () => {
    const pipe = new BooleanPipe();
    expect(pipe).toBeTruthy();
  });
});
