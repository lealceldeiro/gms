import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const  routes: Routes = [
  { path: 'login', loadChildren: './login/login.module#LoginModule'},
  { path: '**', loadChildren: './page-not-found/page-not-found.module#PageNotFoundModule'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
