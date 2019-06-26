import { NgModule } from '@angular/core';
import { DummyStubComponent } from './dummy-stub.component';
import { RouterLinkStubDirective } from './router-link-stub.directive';

/**
 * This module only exist for keeping the standard of declaring every component,directive, etc in a module, so the mock and stub components,
 * directive, etc can be declared here. This module is not intended to be imported anywhere in the rest of the application but (maybe) in
 * some specs.
 */
@NgModule({
  declarations: [RouterLinkStubDirective, DummyStubComponent],
  exports: [RouterLinkStubDirective, DummyStubComponent]
})
export class MockModule { }
