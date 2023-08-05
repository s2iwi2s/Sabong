import { NgModule } from '@angular/core';
import {RouterModule} from "@angular/router";
import { CommonModule } from '@angular/common';


import { HomeComponent } from './home.component';
import {HOME_ROUTE} from "./home.route";



@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([HOME_ROUTE])
  ]
})
export class HomeModule { }
