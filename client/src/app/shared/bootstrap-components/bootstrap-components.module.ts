import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { GmsJumbotronComponent } from './jumbotron/jumbotron.component';
import { PreviewContentComponent } from './preview-content/preview-content.component';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [GmsJumbotronComponent, PreviewContentComponent],
  exports: [GmsJumbotronComponent, PreviewContentComponent]
})
export class GmsBootstrapComponentsModule { }
