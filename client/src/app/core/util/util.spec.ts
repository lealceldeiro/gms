import * as hash from 'object-hash';

import { Util } from './util';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { IPredicate } from '../predicate/predicate.interface';

describe('Util', () => {
  class MockPredicate<T> implements IPredicate<T> {
    test(val: T): boolean {
      return !!val; // a truthy predicate
    }
  }

  it('hashFrom should return a hash from object-hash library from the given input', () => {
    const seed = `test${getRandomNumber()}seed`;

    expect(Util.hashFrom(seed)).toBe(hash(seed));
  });

  it('hashMapFrom should return a clone of the input if it is a primitive', () => {
    // array with number, string, boolean, null and undefined
    const values = [
      getRandomNumber(),
      `test${getRandomNumber()}`,
      getRandomNumber() % 2 === 0,
      null,
      undefined
    ];
    const message = 'failed for primitive ';

    values.forEach(value => {
      expect(Util.hashMapFrom(value)).toEqual(value, message + typeof value);
    });
  });

  it(`hashMapFrom should return a clone of the input when it's a plain object
    and should set all of its keys to their hashed values (calling Util#hash internally)`, () => {
    const returnValueForHash = `sample${getRandomNumber()}value`;
    spyOn(Util, 'hashFrom').and.returnValue(returnValueForHash);
    Util.hashFrom = (): string => returnValueForHash;

    const seed: { [k: string]: string } = {};
    const expectedHashedObject: { [k: string]: string } = {};

    let key: string;
    for (let i = 0; i < getRandomNumber(); i++) {
      key = `test${getRandomNumber()}key`;
      seed[key] = `test${getRandomNumber()}value`;
      expectedHashedObject[key] = returnValueForHash;
    }

    expect(Util.hashMapFrom(seed)).toEqual(expectedHashedObject);
  });

  it('getRandomNumberBetween should give a number between 0 and 100 by default', () => {
    let random;
    for (let i = 0; i < 1000; i++) {
      random = Util.getRandomNumberBetween();
      expect(random).toBeGreaterThanOrEqual(0);
      expect(random).toBeLessThanOrEqual(100);
    }
  });

  it('getRandomNumberBetween should give a number between the specified parameter when these are provided as input', () => {
    let random;
    for (let i = 0; i < 1000; i++) {
      const min = getRandomNumber();
      const max = getRandomNumber(101, 9999999999);
      random = Util.getRandomNumberBetween(min, max);
      expect(random).toBeGreaterThanOrEqual(min);
      expect(random).toBeLessThanOrEqual(max);
    }
  });

  it('allValuesFulfil should return true if all values fulfil certain predicate', () => {
    const values = [true, 'test', getRandomNumber(1, 40), { key: 'value' }, 1];

    expect(Util.allValuesFulfil(new MockPredicate(), ...values)).toBe(true);
  });

  it('allValuesFulfil should return false if at least one value does not fulfil certain predicate', () => {
    const valuesTruthy = [false, 'test', getRandomNumber(1, 40), { key: 'value' }, 1];
    const valuesFalsy1 = [true, undefined, getRandomNumber(1, 40), { key: 'value' }, 1];
    const valuesFalsy2 = [true, 'test', getRandomNumber(1, 40), { key: 'value' }, 0];

    expect(Util.allValuesFulfil(new MockPredicate(), ...valuesTruthy)).toBe(false);
    expect(Util.allValuesFulfil(new MockPredicate(), ...valuesFalsy1)).toBe(false);
    expect(Util.allValuesFulfil(new MockPredicate(), ...valuesFalsy2)).toBe(false);
  });
});
