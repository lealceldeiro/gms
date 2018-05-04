/** Button events to pass to `DebugElement.triggerEventHandler` for RouterLink event handler */
import { DebugElement } from '@angular/core';

export const GmsButtonClickEvents = {
  left:  { button: 0 },
  right: { button: 2 }
};

/** Simulate element click. Defaults to mouse left-button click event. */
export function gmsClick(el: DebugElement | HTMLElement, eventObj: any = GmsButtonClickEvents.left): void {
  if (el instanceof HTMLElement) {
    el.click();
  } else {
    el.triggerEventHandler('click', eventObj);
  }
}
