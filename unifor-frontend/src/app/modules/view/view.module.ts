import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ViewComponent } from './view.component';

// Definindo as rotas
const routes: Routes = [
  {
    path: '',
    component: ViewComponent,
    children: [
      { path: '', redirectTo: 'curriculums', pathMatch: 'full' },
      {
        path: 'curriculums',
        loadChildren: () => import('./curriculum/curriculum-view.module').then(m => m.CurriculumViewModule)
      }
    ]
  }
];

@NgModule({
  declarations: [
    ViewComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class ViewModule { }
