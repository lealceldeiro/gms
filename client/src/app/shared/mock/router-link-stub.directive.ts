import { Directive, HostListener, Input } from '@angular/core';

@Directive({selector: '[routerLink]'})    // tslint:disable-line
export class RouterLinkStubDirective {
  navigatedTo: any = null;

  @Input('routerLink') linkParams: any;   // tslint:disable-line

  @HostListener('click')
  onClick() {
    this.navigatedTo = this.linkParams;
  }
}
