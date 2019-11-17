import { Pipe, PipeTransform } from '@angular/core';

/**
 * A pipe for transforming some value truthy into a human readable way.
 */
@Pipe({ name: 'boolean' })
export class BooleanPipe implements PipeTransform {
  /**
   * Transforms the value to a human readable shape depending on whether its value is falsy or truthy.
   * @param value The from where the truthy is checked.
   * @param trueVal Additional arguments used to show if the evaluation returns truthy. Default: `true`.
   * @param falseVal Additional arguments used to show if the evaluation returns falsy. Default: `false`.
   */
  transform(value: any, trueVal: string = 'true', falseVal: string = 'false'): string {
    return value ? trueVal : falseVal;
  }
}
