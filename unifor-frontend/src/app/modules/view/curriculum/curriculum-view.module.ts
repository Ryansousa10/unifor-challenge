import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { CurriculumViewComponent } from './curriculum-view/curriculum-view.component';
import { CurriculumListViewComponent } from './curriculum-list-view/curriculum-list-view.component';

const routes: Routes = [
  { path: '', component: CurriculumListViewComponent },
  { path: ':id', component: CurriculumViewComponent }
];

@NgModule({
  declarations: [
    CurriculumViewComponent,
    CurriculumListViewComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class CurriculumViewModule { }
