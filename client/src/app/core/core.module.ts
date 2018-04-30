import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from './session/session.service';

@NgModule({
  imports: [CommonModule, FormsModule],
  declarations: [],
  exports: [CommonModule, FormsModule]
})
export class GmsCoreModule {

  /**
   * Method for getting a module's instance
   */
  static forRoot(config?: SessionService): ModuleWithProviders {
    return {
      ngModule: GmsCoreModule,
      providers: [
        { provide: SessionService, useValue: config ? config : new SessionService() }
      ]
    };
  }
}
