import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-semester-form',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h2>{{ isEditMode ? 'Editar' : 'Novo' }} Semestre</h2>
        </div>
        <div class="card-body">
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          
          <form [formGroup]="semesterForm" (ngSubmit)="onSubmit()">
            <div class="form-group">
              <label for="code" class="form-label">Código</label>
              <input type="text" id="code" formControlName="code" class="form-control" 
                     [class.is-invalid]="submitted && f.code.errors">
              <div *ngIf="submitted && f.code.errors" class="invalid-feedback">
                <div *ngIf="f.code.errors.required">Código é obrigatório</div>
              </div>
            </div>

            <div class="form-group">
              <label for="description" class="form-label">Descrição</label>
              <input type="text" id="description" formControlName="description" class="form-control" 
                     [class.is-invalid]="submitted && f.description.errors">
              <div *ngIf="submitted && f.description.errors" class="invalid-feedback">
                <div *ngIf="f.description.errors.required">Descrição é obrigatória</div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label for="startDate" class="form-label">Data de Início</label>
                  <input type="date" id="startDate" formControlName="startDate" class="form-control" 
                         [class.is-invalid]="submitted && f.startDate.errors">
                  <div *ngIf="submitted && f.startDate.errors" class="invalid-feedback">
                    <div *ngIf="f.startDate.errors.required">Data de início é obrigatória</div>
                  </div>
                </div>
              </div>
              
              <div class="col-md-6">
                <div class="form-group">
                  <label for="endDate" class="form-label">Data de Término</label>
                  <input type="date" id="endDate" formControlName="endDate" class="form-control" 
                         [class.is-invalid]="submitted && f.endDate.errors">
                  <div *ngIf="submitted && f.endDate.errors" class="invalid-feedback">
                    <div *ngIf="f.endDate.errors.required">Data de término é obrigatória</div>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label for="active" class="form-label">Status</label>
              <div class="form-check">
                <input type="checkbox" id="active" formControlName="active" class="form-check-input">
                <label class="form-check-label" for="active">Ativo</label>
              </div>
            </div>

            <div class="form-group mt-4">
              <button type="submit" class="btn btn-primary mr-2" [disabled]="loading">
                <span *ngIf="loading" class="spinner-border spinner-border-sm mr-1"></span>
                Salvar
              </button>
              <button type="button" class="btn btn-secondary" (click)="goBack()">Voltar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .mr-1 { margin-right: 0.25rem; }
    .mr-2 { margin-right: 0.5rem; }
  `]
})
export class SemesterFormComponent implements OnInit {
  semesterForm!: FormGroup;
  isEditMode = false;
  semesterId: string | null = null;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private academicService: AcademicService
  ) { }

  ngOnInit(): void {
    this.semesterForm = this.formBuilder.group({
      code: ['', Validators.required],
      description: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      active: [true]
    });

    this.semesterId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.semesterId;

    if (this.isEditMode && this.semesterId) {
      this.loadSemester(this.semesterId);
    }
  }

  loadSemester(id: string): void {
    this.loading = true;
    this.academicService.getSemester(id).subscribe({
      next: (data) => {
        // Formato de data para input type="date": YYYY-MM-DD
        const startDate = data.startDate ? new Date(data.startDate).toISOString().split('T')[0] : '';
        const endDate = data.endDate ? new Date(data.endDate).toISOString().split('T')[0] : '';

        this.semesterForm.patchValue({
          code: data.code,
          description: data.description,
          startDate: startDate,
          endDate: endDate,
          active: data.active
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao carregar semestre:', err);
      }
    });
  }

  get f() { return this.semesterForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    if (this.semesterForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.semesterId) {
      this.updateSemester();
    } else {
      this.createSemester();
    }
  }

  createSemester(): void {
    this.academicService.createSemester(this.semesterForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/coordinator/semesters']);
      },
      error: (err) => {
        this.error = 'Erro ao criar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao criar semestre:', err);
      }
    });
  }

  updateSemester(): void {
    if (!this.semesterId) return;

    this.academicService.updateSemester(this.semesterId, this.semesterForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/coordinator/semesters']);
      },
      error: (err) => {
        this.error = 'Erro ao atualizar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao atualizar semestre:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/coordinator/semesters']);
  }
}
