import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NgbCollapseModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AlertComponent } from './alert/alert.component';
import { JumbotronComponent } from './jumbotron/jumbotron.component';
import { PaginationComponent } from './pagination/pagination.component';
import { PreviewContentComponent } from './preview-content/preview-content.component';

@NgModule({
  imports: [CommonModule, RouterModule, NgbModule, NgbCollapseModule],
  declarations: [JumbotronComponent, PreviewContentComponent, PaginationComponent, AlertComponent],
  exports: [
    NgbModule,
    NgbCollapseModule,
    JumbotronComponent,
    PreviewContentComponent,
    PaginationComponent,
    AlertComponent
  ]
})
export class BootstrapComponentsModule {
}
