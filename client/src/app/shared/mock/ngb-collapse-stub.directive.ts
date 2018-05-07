import { Directive, Input } from '@angular/core';

/**
 * A stub (mock) directive for simulating the `[ngbCollapse]` directive in the specs.
 */
@Directive({selector: '[ngbCollapse]'})     // tslint:disable-line
export class NgbCollapseStubDirective {

  /**
   * ngbCollapse directive simulation.
   * @type {boolean}
   */
  @Input('ngbCollapse') isCollapsed = true; // tslint:disable-line
}
