import * as _ from 'lodash';
import * as hash from 'object-hash';

import { IPredicate } from '../predicate/predicate.interface';

/**
 * Utility class.
 */
export class Util {
  /**
   * Generate a hash from a T value.
   *
   * @param seed Value to generate the hash from.
   * @returns A hash string.
   */
  static hashFrom<T>(seed: T): string {
    return hash(seed);
  }

  /**
   * Takes an input as a seed and iterates over all of the keys of the object and replace the value of the key for the hash
   * generated from the previous value associated to the same key. This does not modify the original object.
   *
   * @param seed Object T to iterate over all of its keys.
   * @returns A cloned object from the provided object T as argument with the key replaced by the hashes.
   */
  static hashMapFrom<T>(seed: T): T {
    const clone = _.cloneDeep(seed);
    if (_.isPlainObject(seed)) {
      _.keys(clone).forEach(key => {
        const k = key as keyof T;
        clone[k] = this.hashFrom(clone[k]) as any;
      });
    }

    return clone;
  }

  /**
   * Produces a random number between the two specified limits (`min` | default 0, `max` | default 100).
   * @param min Number to take as minimum value in the range where the number will be generated in.
   * @param max Number to take as maximum value in the range where the number will be generated in.
   *
   * @returns A random `number` between the numbers `min` and `max` specified as arguments.
   */
  static getRandomNumberBetween(min: number = 0, max: number = 100): number {
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  /**
   * Checks that all `values` fulfil the given predicate.
   *
   * @param values values to check the predicate against to.
   * @param predicate Predicate to check against the value.
   * @returns true if all `values` fulfil the given `predicate`, false otherwise.
   */
  static allValuesFulfil<T>(predicate: IPredicate<T>, ...values: T[]): boolean {
    return _.every(values, (val) => predicate.test(val));
  }
}
