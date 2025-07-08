import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CurriculumListComponent } from './curriculum-list/curriculum-list.component';
import { CurriculumFormComponent } from './curriculum-form/curriculum-form.component';
import { CurriculumDetailsComponent } from './curriculum-details/curriculum-details.component';

const routes: Routes = [
  { path: '', component: CurriculumListComponent },
  { path: 'new', component: CurriculumFormComponent },
  { path: 'edit/:id', component: CurriculumFormComponent },
  { path: ':id', component: CurriculumDetailsComponent }
];

@NgModule({
  declarations: [
    CurriculumListComponent,
    CurriculumFormComponent,
    CurriculumDetailsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class CurriculumsModule { }
