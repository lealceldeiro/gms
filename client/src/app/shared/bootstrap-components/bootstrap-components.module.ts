import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule, NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { PaginationComponent } from './pagination/pagination.component';
import { JumbotronComponent } from './jumbotron/jumbotron.component';
import { PreviewContentComponent } from './preview-content/preview-content.component';
import { AlertComponent } from './alert/alert.component';

@NgModule({
  imports: [CommonModule, RouterModule, NgbModule, NgbCollapseModule],
  declarations: [JumbotronComponent, PreviewContentComponent, PaginationComponent, AlertComponent],
  exports: [NgbModule, NgbCollapseModule, JumbotronComponent, PreviewContentComponent, PaginationComponent, AlertComponent]
})
export class BootstrapComponentsModule { }
