import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomeModule } from './home/home.module';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SharedModule } from './shared/shared.module';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent
  ],
  imports: [
    BrowserModule,
    NgbModule.forRoot(),
    HomeModule,
    AppRoutingModule,
    SharedModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
