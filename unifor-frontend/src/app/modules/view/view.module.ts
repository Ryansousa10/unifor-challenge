import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ViewComponent } from './view.component';
import { FormsModule } from '@angular/forms';

// Definindo as rotas
const routes: Routes = [
  {
    path: 'curriculums',
    component: ViewComponent
  },
  {
    path: '',
    redirectTo: 'curriculums',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    ViewComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule
  ]
})
export class ViewModule { }
