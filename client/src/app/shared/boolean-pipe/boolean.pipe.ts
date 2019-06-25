import { Pipe, PipeTransform } from '@angular/core';

/**
 * A pipe for transforming some value truthy into a human readable way.
 */
@Pipe({ name: 'boolean' })
export class BooleanPipe implements PipeTransform {

  /**
   * Transforms the value to a human readable shape depending on whether its value is falsy or truthy.
   * @param value The from where the truthy is checked.
   * @param args Additional arguments. args[0] is currently used as arguments to show if the evaluation returns truthy, args[1] falsy.
   */
  transform(value: any, trueVal: string = 'true', falseVal = 'false'): string {
    return value ? trueVal : falseVal;
  }

}
