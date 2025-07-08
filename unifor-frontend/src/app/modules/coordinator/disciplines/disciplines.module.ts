import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DisciplineListComponent } from './discipline-list/discipline-list.component';
import { DisciplineFormComponent } from './discipline-form/discipline-form.component';

const routes: Routes = [
  { path: '', component: DisciplineListComponent },
  { path: 'new', component: DisciplineFormComponent },
  { path: 'edit/:id', component: DisciplineFormComponent }
];

@NgModule({
  declarations: [
    DisciplineListComponent,
    DisciplineFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class DisciplinesModule { }
