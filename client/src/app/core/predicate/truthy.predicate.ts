import { IPredicate } from './predicate.interface';

/**
 * Predicate to check that a value is truthy.
 */
export class TruthyPredicate<T> implements IPredicate<T> {
  /**
   * Test whether `data` has a truthy value;
   *
   * @param {T} data Data to be tested against.
   * @returns {boolean} `true` if `data` is truthy, `false`, otherwise.
   */
  test(data: T): boolean {
    return !!data;
  }
}
