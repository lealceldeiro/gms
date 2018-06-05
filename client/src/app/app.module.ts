import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { CookieModule } from 'ngx-cookie';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SideMenuComponent } from './side-menu/side-menu.component';
import { SharedModule } from './shared/shared.module';
import { GmsCoreModule } from './core/core.module';
import { HomeModule } from './home/home.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

/**
 * Base module, bootstrapped in the main file.
 */
@NgModule({
  declarations: [ AppComponent, NavBarComponent, SideMenuComponent ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,  // required by toastr
    ToastrModule.forRoot({ preventDuplicates: true, autoDismiss: true, enableHtml: true }),
    CookieModule.forRoot(),
    NgbModule.forRoot(),
    SharedModule,
    GmsCoreModule.forRoot(),
    HomeModule,
    AppRoutingModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
