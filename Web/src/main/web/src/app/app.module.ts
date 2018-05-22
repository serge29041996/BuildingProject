import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BuildingService } from './building/building.service';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { BuildingListComponent } from './building-list/building-list.component';
import {SafeHtml} from "./saveHtml.pipe";
import { BuildingFormComponent } from './building-form/building-form.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import {AppRoutingModule} from "./app-routing.module";
import { HeaderComponent } from './header/header.component';


@NgModule({
  declarations: [
    AppComponent,
    BuildingListComponent,
    SafeHtml,
    BuildingFormComponent,
    ErrorPageComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [BuildingService],
  bootstrap: [AppComponent]
})
export class AppModule { }
