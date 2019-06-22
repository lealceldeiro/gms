import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfigurationListComponent } from './configuration-list/configuration-list.component';

/**
 * Routes handled in this module.
 */
const routes: Routes = [
  { path: 'list', component: ConfigurationListComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class ConfigurationsRoutingModule { }
