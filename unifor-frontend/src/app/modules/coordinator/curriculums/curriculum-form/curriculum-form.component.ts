import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-curriculum-form',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h2>{{ isEditMode ? 'Editar' : 'Nova' }} Matriz Curricular</h2>
        </div>
        <div class="card-body">
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          
          <form [formGroup]="curriculumForm" (ngSubmit)="onSubmit()">
            <div class="form-group">
              <label for="name" class="form-label">Nome</label>
              <input type="text" id="name" formControlName="name" class="form-control" 
                     [ngClass]="{'is-invalid': submitted && f.name.errors}">
              <div *ngIf="submitted && f.name.errors" class="invalid-feedback">
                <div *ngIf="f.name.errors.required">Nome é obrigatório</div>
              </div>
            </div>

            <div class="form-group">
              <label for="courseId" class="form-label">Curso</label>
              <select id="courseId" formControlName="courseId" class="form-control"
                      [ngClass]="{'is-invalid': submitted && f.courseId.errors}">
                <option value="">Selecione um curso</option>
                <option *ngFor="let course of courses" [value]="course.id">
                  {{ course.name }}
                </option>
              </select>
              <div *ngIf="submitted && f.courseId.errors" class="invalid-feedback">
                <div *ngIf="f.courseId.errors.required">Curso é obrigatório</div>
              </div>
            </div>

            <div class="form-group">
              <label for="active" class="form-label">Status</label>
              <div class="form-check">
                <input type="checkbox" id="active" formControlName="active" class="form-check-input">
                <label class="form-check-label" for="active">Ativo</label>
              </div>
            </div>

            <div class="form-group">
              <label for="description" class="form-label">Descrição</label>
              <textarea id="description" formControlName="description" class="form-control" rows="3"></textarea>
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
export class CurriculumFormComponent implements OnInit {
  curriculumForm: FormGroup;
  isEditMode = false;
  curriculumId: string | null = null;
  loading = false;
  submitted = false;
  error = '';
  courses: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private curriculumService: CurriculumService,
    private academicService: AcademicService
  ) {
    this.curriculumForm = this.formBuilder.group({
      name: ['', Validators.required],
      courseId: ['', Validators.required],
      active: [true],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.loadCourses();

    this.curriculumId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.curriculumId;

    if (this.isEditMode && this.curriculumId) {
      this.loadCurriculum(this.curriculumId);
    }
  }

  loadCourses(): void {
    this.academicService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;
      },
      error: (err) => {
        this.error = 'Erro ao carregar cursos. Por favor, tente novamente.';
        console.error('Erro ao carregar cursos:', err);
      }
    });
  }

  loadCurriculum(id: string): void {
    this.loading = true;
    this.curriculumService.getCurriculum(id).subscribe({
      next: (data) => {
        this.curriculumForm.patchValue({
          name: data.name,
          courseId: data.courseId,
          active: data.active,
          description: data.description
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar matriz curricular. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao carregar matriz curricular:', err);
      }
    });
  }

  get f() { return this.curriculumForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    if (this.curriculumForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.curriculumId) {
      this.updateCurriculum();
    } else {
      this.createCurriculum();
    }
  }

  createCurriculum(): void {
    this.curriculumService.createCurriculum(this.curriculumForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/coordinator/curriculums']);
      },
      error: (err) => {
        this.error = 'Erro ao criar matriz curricular. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao criar matriz curricular:', err);
      }
    });
  }

  updateCurriculum(): void {
    if (!this.curriculumId) return;

    this.curriculumService.updateCurriculum(this.curriculumId, this.curriculumForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/coordinator/curriculums']);
      },
      error: (err) => {
        this.error = 'Erro ao atualizar matriz curricular. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao atualizar matriz curricular:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/coordinator/curriculums']);
  }
}
