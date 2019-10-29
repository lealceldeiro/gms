/**
 * Represents a predicate (boolean-valued function) of one argument. It can be see asn a
 * functional interface whose functional method is test(T)
 */
export interface IPredicate<T> {

    /**
     * Test the predicate.
     *
     * @param {T} data Data to be tested against.
     * @returns {boolean} `true` if `data` is truthy, `false`, otherwise.
     */
    test(data: T): boolean;
}
