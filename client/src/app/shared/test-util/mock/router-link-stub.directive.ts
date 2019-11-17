import { Directive, HostListener, Input } from '@angular/core';

/**
 * A stub (mock) directive for simulating the `[routeLink]` directive in the specs.
 */
@Directive({ selector: '[routerLink]' }) // tslint:disable-line
export class RouterLinkStubDirective {
  /**
   * Url where the router navigated to.
   */
  navigatedTo: any = null;

  /**
   * routerLink directive simulation.
   */
  @Input('routerLink') linkParams: any;

  /**
   * Listener to the `click` event on the element where there are `routerLink` directives applied.
   */
  @HostListener('click')
  onClick() {
    this.navigatedTo = this.linkParams;
  }
}
