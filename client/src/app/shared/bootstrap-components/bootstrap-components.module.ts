import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { GmsPaginationComponent } from './gms-pagination/gms-pagination.component';
import { GmsJumbotronComponent } from './jumbotron/jumbotron.component';
import { PreviewContentComponent } from './preview-content/preview-content.component';

@NgModule({
  imports: [CommonModule, RouterModule, NgbModule],
  declarations: [GmsJumbotronComponent, PreviewContentComponent, GmsPaginationComponent],
  exports: [GmsJumbotronComponent, PreviewContentComponent, GmsPaginationComponent]
})
export class GmsBootstrapComponentsModule { }
