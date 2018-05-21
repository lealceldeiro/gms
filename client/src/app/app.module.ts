import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomeModule } from './home/home.module';
import { HttpClientModule } from '@angular/common/http';
import { CookieModule } from 'ngx-cookie';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SideMenuComponent } from './side-menu/side-menu.component';
import { SharedModule } from './shared/shared.module';
import { GmsCoreModule } from './core/core.module';

/**
 * Base module, bootstrapped in the main file.
 */
@NgModule({
  declarations: [ AppComponent, NavBarComponent, SideMenuComponent ],
  imports: [
    BrowserModule,
    HttpClientModule,
    CookieModule.forRoot(),
    SharedModule,
    NgbModule.forRoot(),
    GmsCoreModule.forRoot(),
    HomeModule,
    AppRoutingModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
