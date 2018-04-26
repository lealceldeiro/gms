import { NgModule } from '@angular/core';
import { GmsJumbotronComponent } from './jumbotron/jumbotron.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [GmsJumbotronComponent],
  exports: [GmsJumbotronComponent]
})
export class GmsBootstrapComponentsModule { }
