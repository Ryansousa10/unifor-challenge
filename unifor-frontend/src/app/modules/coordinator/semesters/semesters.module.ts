import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SemesterListComponent } from './semester-list/semester-list.component';
import { SemesterFormComponent } from './semester-form/semester-form.component';

const routes: Routes = [
  { path: '', component: SemesterListComponent },
  { path: 'new', component: SemesterFormComponent },
  { path: 'edit/:id', component: SemesterFormComponent }
];

@NgModule({
  declarations: [
    SemesterListComponent,
    SemesterFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class SemestersModule { }
