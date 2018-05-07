import { NgModule } from '@angular/core';
import { NgbCollapseStubDirective } from './ngb-collapse-stub.directive';
import { RouterLinkStubDirective } from './router-link-stub.directive';
import { DummyStubComponent } from './dummy-stub.component';

@NgModule({
  declarations: [ NgbCollapseStubDirective, RouterLinkStubDirective, DummyStubComponent ],
  exports: [ NgbCollapseStubDirective, RouterLinkStubDirective, DummyStubComponent ]
})
export class MockModule { }
