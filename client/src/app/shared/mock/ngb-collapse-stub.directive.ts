import { Directive, Input } from '@angular/core';

@Directive({selector: '[ngbCollapse]'})     // tslint:disable-line
export class NgbCollapseStubDirective {
  @Input('ngbCollapse') isCollapsed = true; // tslint:disable-line
}
