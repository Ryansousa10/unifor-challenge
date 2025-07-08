import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';

@Component({
  selector: 'app-curriculum-details',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header d-flex justify-between align-center">
          <h2>Detalhes da Matriz Curricular</h2>
          <div>
            <button class="btn btn-primary mr-2" (click)="editCurriculum()">
              <i class="fa fa-edit"></i> Editar
            </button>
            <button class="btn btn-secondary" (click)="goBack()">
              <i class="fa fa-arrow-left"></i> Voltar
            </button>
          </div>
        </div>
        
        <div class="card-body">
          <div *ngIf="loading" class="text-center">
            <p>Carregando detalhes...</p>
          </div>
          
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          
          <div *ngIf="!loading && !error && curriculum">
            <div class="row mb-4">
              <div class="col-md-6">
                <h4>Informações Gerais</h4>
                <table class="table">
                  <tbody>
                    <tr>
                      <th>Nome:</th>
                      <td>{{ curriculum.name }}</td>
                    </tr>
                    <tr>
                      <th>Curso:</th>
                      <td>{{ curriculum.courseName }}</td>
                    </tr>
                    <tr>
                      <th>Status:</th>
                      <td>
                        <span [ngClass]="curriculum.active ? 'badge badge-success' : 'badge badge-secondary'">
                          {{ curriculum.active ? 'Ativo' : 'Inativo' }}
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <th>Descrição:</th>
                      <td>{{ curriculum.description || 'Nenhuma descrição fornecida' }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            
            <h4>Disciplinas da Matriz Curricular</h4>
            <div *ngIf="curriculum.disciplines && curriculum.disciplines.length > 0">
              <table class="table">
                <thead>
                  <tr>
                    <th>Código</th>
                    <th>Nome</th>
                    <th>Semestre</th>
                    <th>Créditos</th>
                    <th>Carga Horária</th>
                    <th>Tipo</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let discipline of curriculum.disciplines">
                    <td>{{ discipline.code }}</td>
                    <td>{{ discipline.name }}</td>
                    <td>{{ discipline.semester }}</td>
                    <td>{{ discipline.credits }}</td>
                    <td>{{ discipline.workload }}</td>
                    <td>
                      <span [ngClass]="discipline.optional ? 'badge badge-info' : 'badge badge-primary'">
                        {{ discipline.optional ? 'Optativa' : 'Obrigatória' }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <div *ngIf="!curriculum.disciplines || curriculum.disciplines.length === 0" class="alert alert-info">
              Nenhuma disciplina cadastrada nesta matriz curricular.
            </div>
            
            <div class="mt-3">
              <button class="btn btn-success" (click)="addDiscipline()">
                <i class="fa fa-plus"></i> Adicionar Disciplina
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .mr-2 { margin-right: 0.5rem; }
  `]
})
export class CurriculumDetailsComponent implements OnInit {
  curriculumId: string | null = null;
  curriculum: any = null;
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private curriculumService: CurriculumService
  ) { }

  ngOnInit(): void {
    this.curriculumId = this.route.snapshot.paramMap.get('id');
    if (this.curriculumId) {
      this.loadCurriculum(this.curriculumId);
    } else {
      this.error = 'ID da matriz curricular não fornecido.';
    }
  }

  loadCurriculum(id: string): void {
    this.loading = true;
    this.error = '';

    this.curriculumService.getCurriculumWithDisciplines(id).subscribe({
      next: (data) => {
        this.curriculum = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar detalhes da matriz curricular. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao carregar matriz curricular:', err);
      }
    });
  }

  editCurriculum(): void {
    if (this.curriculumId) {
      this.router.navigate([`/coordinator/curriculums/edit/${this.curriculumId}`]);
    }
  }

  addDiscipline(): void {
    if (this.curriculumId) {
      this.router.navigate([`/coordinator/curriculums/${this.curriculumId}/disciplines/add`]);
    }
  }

  goBack(): void {
    this.router.navigate(['/coordinator/curriculums']);
  }
}
