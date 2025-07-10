import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { CurriculumDetailsComponent } from './modules/coordinator/curriculums/curriculum-details/curriculum-details.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'coordinator',
    loadChildren: () => import('./modules/coordinator/coordinator.module').then(m => m.CoordinatorModule),
    canActivate: [AuthGuard],
    data: { roles: ['COORDENADOR'] }
  },
  {
    path: 'view',
    loadChildren: () => import('./modules/view/view.module').then(m => m.ViewModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'coordinator/curriculums/:id',
    component: CurriculumDetailsComponent,
    canActivate: [AuthGuard],
    data: { roles: ['COORDENADOR', 'PROFESSOR', 'ALUNO'] }
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
