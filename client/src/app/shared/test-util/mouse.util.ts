import { DebugElement } from '@angular/core';

/**
 * Useful constant which mocks the button events to pass to `DebugElement.triggerEventHandler` for RouterLink event handler.
 * @type {{}}
 */
export const GmsButtonClickEvents = {
  left:  { button: 0 },
  right: { button: 2 }
};

/** Simulate element click. Defaults to mouse left-button click event.
 * @param el Element to be clicked.
 * @param eventObj Event which produced the click.
 */
export function gmsClick(el: DebugElement | HTMLElement, eventObj: any = GmsButtonClickEvents.left): void {
  if (el instanceof HTMLElement) {
    el.click();
  } else {
    el.triggerEventHandler('click', eventObj);
  }
}
