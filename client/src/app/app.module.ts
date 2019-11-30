import { HttpClientModule } from '@angular/common/http';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CookieModule } from 'ngx-cookie';
import { ToastrModule } from 'ngx-toastr';
import { NgxUiLoaderModule } from 'ngx-ui-loader';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppConfig } from './core/config/app.config';
import { GmsCoreModule } from './core/core.module';
import { HomeModule } from './home/home.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SharedModule } from './shared/shared.module';
import { SideMenuComponent } from './side-menu/side-menu.component';

/**
 * Factory for loading the app configuration at startup.
 *
 * @param appConfig AppConfig instance.
 */
export function initializeApp(appConfig: AppConfig): () => Promise<void> {
  return () => appConfig.load();
}

/**
 * Base module, bootstrapped in the main file.
 */
@NgModule({
  declarations: [AppComponent, NavBarComponent, SideMenuComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgxUiLoaderModule, // "loader" (spinner) component
    BrowserAnimationsModule, // required by toastr
    ToastrModule.forRoot({ preventDuplicates: true, autoDismiss: true, enableHtml: true }),
    CookieModule.forRoot(),
    NgbModule,
    SharedModule,
    GmsCoreModule.forRoot(),
    HomeModule,
    AppRoutingModule
  ],
  providers: [
    AppConfig,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [AppConfig],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
