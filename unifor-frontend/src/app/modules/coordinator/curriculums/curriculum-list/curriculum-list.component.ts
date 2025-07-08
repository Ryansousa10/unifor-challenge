import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';

@Component({
  selector: 'app-curriculum-list',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header d-flex justify-between align-center">
          <h2>Matrizes Curriculares</h2>
          <button class="btn btn-primary" (click)="navigateToNew()">
            <i class="fa fa-plus"></i> Nova Matriz Curricular
          </button>
        </div>
        <div class="card-body">
          <div *ngIf="loading" class="text-center">
            <p>Carregando matrizes curriculares...</p>
          </div>
          
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          
          <div *ngIf="!loading && !error && curriculums.length === 0" class="alert alert-info">
            Nenhuma matriz curricular encontrada.
          </div>
          
          <table *ngIf="!loading && curriculums.length > 0" class="table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Curso</th>
                <th>Ativo</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let curriculum of curriculums">
                <td>{{ curriculum.name }}</td>
                <td>{{ curriculum.courseName }}</td>
                <td>
                  <span [ngClass]="curriculum.active ? 'badge badge-success' : 'badge badge-secondary'">
                    {{ curriculum.active ? 'Sim' : 'Não' }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-sm btn-info mr-1" (click)="viewDetails(curriculum.id)">
                    <i class="fa fa-eye"></i>
                  </button>
                  <button class="btn btn-sm btn-primary mr-1" (click)="editCurriculum(curriculum.id)">
                    <i class="fa fa-edit"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .mr-1 {
      margin-right: 0.5rem;
    }
  `]
})
export class CurriculumListComponent implements OnInit {
  curriculums: any[] = [];
  loading = false;
  error = '';

  constructor(
    private curriculumService: CurriculumService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCurriculums();
  }

  loadCurriculums(): void {
    this.loading = true;
    this.error = '';

    this.curriculumService.getCurriculums().subscribe({
      next: (data) => {
        this.curriculums = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar matrizes curriculares. Tente novamente mais tarde.';
        this.loading = false;
        console.error('Erro ao carregar matrizes curriculares:', err);
      }
    });
  }

  navigateToNew(): void {
    this.router.navigate(['/coordinator/curriculums/new']);
  }

  editCurriculum(id: string): void {
    this.router.navigate([`/coordinator/curriculums/edit/${id}`]);
  }

  viewDetails(id: string): void {
    this.router.navigate([`/coordinator/curriculums/details/${id}`]);
  }
}
