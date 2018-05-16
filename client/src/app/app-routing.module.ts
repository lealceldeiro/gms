import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from './core/guard/login.guard';

const  routes: Routes = [
  { path: 'login', loadChildren: './login/gms-login.module#GmsLoginModule', canActivateChild: [LoginGuard], canLoad: [LoginGuard]},
  { path: '**', loadChildren: './page-not-found/page-not-found.module#PageNotFoundModule'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
