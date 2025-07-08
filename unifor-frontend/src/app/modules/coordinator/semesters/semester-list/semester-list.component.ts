import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-semester-list',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header d-flex justify-between align-center">
          <h2>Semestres</h2>
          <button class="btn btn-primary" (click)="navigateToNew()">
            <i class="fa fa-plus"></i> Novo Semestre
          </button>
        </div>
        <div class="card-body">
          <div *ngIf="loading" class="text-center">
            <p>Carregando semestres...</p>
          </div>
          
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          
          <div *ngIf="!loading && !error && semesters.length === 0" class="alert alert-info">
            Nenhum semestre encontrado.
          </div>
          
          <table *ngIf="!loading && semesters.length > 0" class="table">
            <thead>
              <tr>
                <th>Código</th>
                <th>Descrição</th>
                <th>Data de Início</th>
                <th>Data de Término</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let semester of semesters">
                <td>{{ semester.code }}</td>
                <td>{{ semester.description }}</td>
                <td>{{ semester.startDate | date:'dd/MM/yyyy' }}</td>
                <td>{{ semester.endDate | date:'dd/MM/yyyy' }}</td>
                <td>
                  <span class="badge" [class.badge-success]="semester.active" [class.badge-secondary]="!semester.active">
                    {{ semester.active ? 'Ativo' : 'Inativo' }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-sm btn-primary mr-1" (click)="editSemester(semester.id)">
                    <i class="fa fa-edit"></i>
                  </button>
                  <button class="btn btn-sm btn-danger" (click)="deleteSemester(semester.id)">
                    <i class="fa fa-trash"></i>
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
    .d-flex {
      display: flex;
    }
    .justify-between {
      justify-content: space-between;
    }
    .align-center {
      align-items: center;
    }
  `]
})
export class SemesterListComponent implements OnInit {
  semesters: any[] = [];
  loading = false;
  error = '';

  constructor(
    private academicService: AcademicService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadSemesters();
  }

  loadSemesters(): void {
    this.loading = true;
    this.error = '';

    this.academicService.getSemesters().subscribe({
      next: (data) => {
        this.semesters = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar semestres. Tente novamente mais tarde.';
        this.loading = false;
        console.error('Erro ao carregar semestres:', err);
      }
    });
  }

  navigateToNew(): void {
    this.router.navigate(['/coordinator/semesters/new']);
  }

  editSemester(id: string): void {
    this.router.navigate([`/coordinator/semesters/edit/${id}`]);
  }

  deleteSemester(id: string): void {
    if (confirm('Tem certeza que deseja excluir este semestre?')) {
      this.academicService.deleteSemester(id).subscribe({
        next: () => {
          this.loadSemesters();
        },
        error: (err) => {
          this.error = 'Erro ao excluir semestre. Tente novamente mais tarde.';
          console.error('Erro ao excluir semestre:', err);
        }
      });
    }
  }
}
