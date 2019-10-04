/** Returns a random number between the specified min and max values.
 * @param min Range bottom | default `0`.
 * @param max Range top | default `100`.
 */
export function getRandomNumber(min: number = 0, max: number = 100): number {
  return Math.floor(Math.random() * (max - min + 1) + min);
}
