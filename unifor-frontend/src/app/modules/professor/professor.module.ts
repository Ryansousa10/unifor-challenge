import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProfessorComponent } from './professor.component';

const routes: Routes = [
  {
    path: '',
    component: ProfessorComponent
  }
];

@NgModule({
  declarations: [
    ProfessorComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class ProfessorModule { }
