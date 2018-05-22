import {Routes, RouterModule} from "@angular/router";
import {BuildingListComponent} from "./building-list/building-list.component";
import {BuildingFormComponent} from "./building-form/building-form.component";
import {ErrorPageComponent} from "./error-page/error-page.component";
import {NgModule} from "@angular/core";

const appRoutes: Routes = [
  {path: '', component: BuildingListComponent},
  {path: 'building/:operation', component: BuildingFormComponent},
  {path: 'building/:operation/:id', component: BuildingFormComponent},
  {path: 'not-found', component: ErrorPageComponent},
  {path: '**', redirectTo: '/not-found'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
