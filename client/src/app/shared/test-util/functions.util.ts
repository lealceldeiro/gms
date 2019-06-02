/** Returns a random number between the specified min and max values.
 * @param min Range bottom.
 * @param max Range top.
 */
export function getRandomNumber(min: number = 0, max: number = 100): number {
  return Math.floor(Math.random() * (max - min + 1) + min);
}
