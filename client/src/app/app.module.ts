import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomeModule } from './home/home.module';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SharedModule } from './shared/shared.module';
import { SideMenuComponent } from './side-menu/side-menu.component';
import { GmsCoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';

/**
 * Base module, bootstrapped in the main file.
 */
@NgModule({
  declarations: [ AppComponent, NavBarComponent, SideMenuComponent ],
  imports: [
    BrowserModule,
    HttpClientModule,
    SharedModule,
    NgbModule.forRoot(),
    GmsCoreModule.forRoot(),
    HomeModule,
    AppRoutingModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
