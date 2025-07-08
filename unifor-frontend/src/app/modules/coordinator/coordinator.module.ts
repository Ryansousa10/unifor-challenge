import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoordinatorComponent } from './coordinator.component';

// Definindo as rotas
const routes: Routes = [
  {
    path: '',
    component: CoordinatorComponent,
    children: [
      { path: '', redirectTo: 'courses', pathMatch: 'full' },
      {
        path: 'courses',
        loadChildren: () => import('./courses/courses.module').then(m => m.CoursesModule)
      },
      {
        path: 'disciplines',
        loadChildren: () => import('./disciplines/disciplines.module').then(m => m.DisciplinesModule)
      },
      {
        path: 'semesters',
        loadChildren: () => import('./semesters/semesters.module').then(m => m.SemestersModule)
      },
      {
        path: 'curriculums',
        loadChildren: () => import('./curriculums/curriculums.module').then(m => m.CurriculumsModule)
      }
    ]
  }
];

@NgModule({
  declarations: [
    CoordinatorComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class CoordinatorModule { }
