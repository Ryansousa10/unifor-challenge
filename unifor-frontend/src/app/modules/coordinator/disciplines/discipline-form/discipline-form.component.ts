import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Discipline } from '../../../../models/academic.model';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-discipline-form',
  templateUrl: './discipline-form.component.html',
  styleUrls: ['./discipline-form.component.css']
})
export class DisciplineFormComponent implements OnInit {
  disciplineForm!: FormGroup;
  isEditMode = false;
  disciplineId?: string;
  loading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private academicService: AcademicService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initForm();

    // Verificar se estamos no modo de edição
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.disciplineId = id;
      this.loadDisciplineData(this.disciplineId);
    }
  }

  initForm(): void {
    this.disciplineForm = this.fb.group({
      name: ['', [Validators.required]],
      code: ['', [Validators.required]],
      workload: ['', [Validators.required, Validators.min(1)]],
      credits: ['', [Validators.required, Validators.min(1), Validators.max(10)]],
      description: ['']
    });
  }

  loadDisciplineData(id: string): void {
    this.loading = true;
    this.academicService.getDiscipline(id).subscribe({
      next: (discipline) => {
        this.disciplineForm.patchValue({
          name: discipline.name,
          code: discipline.code,
          workload: discipline.workload,
          credits: discipline.credits,
          description: discipline.description
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar dados da disciplina.';
        console.error('Erro ao buscar disciplina:', err);
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.disciplineForm.invalid) {
      this.disciplineForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    const formData = this.disciplineForm.value;

    const workload = formData.workload !== null && formData.workload !== undefined && formData.workload !== ''
      ? Number(formData.workload) : 0;
    const credits = formData.credits !== null && formData.credits !== undefined && formData.credits !== ''
      ? Number(formData.credits) : 0;

    const discipline: Discipline = {
      name: formData.name,
      code: formData.code,
      workload: workload,
      credits: credits,
      description: formData.description
    };

    if (this.isEditMode && this.disciplineId) {
      this.academicService.updateDiscipline(this.disciplineId, discipline).subscribe({
        next: () => {
          this.success = 'Disciplina atualizada com sucesso!';
          this.loading = false;
          setTimeout(() => this.navigateBack(), 2000);
        },
        error: (err) => {
          this.error = 'Erro ao atualizar disciplina.';
          console.error('Erro ao atualizar disciplina:', err);
          this.loading = false;
        }
      });
    } else {
      this.academicService.createDiscipline(discipline).subscribe({
        next: () => {
          this.success = 'Disciplina criada com sucesso!';
          this.loading = false;
          setTimeout(() => this.navigateBack(), 2000);
        },
        error: (err) => this.handleSubmitError(err)
      });
    }
  }

  private handleSubmitError(err: any) {
    let errorMsg = (err.error?.message || err.message || JSON.stringify(err)).toLowerCase();
    if (
      err.status === 409 ||
      (typeof err === 'string' && err.includes('409')) ||
      (err.error && typeof err.error === 'string' && (err.error.includes('409') || err.error.includes('conflict')))
    ) {
      if (errorMsg.includes('código') || errorMsg.includes('code')) {
        this.error = 'Já existe uma disciplina cadastrada com este código.';
      } else if (errorMsg.includes('name') || errorMsg.includes('nome')) {
        this.error = 'Já existe uma disciplina cadastrada com este nome.';
      } else {
        this.error = 'Já existe uma disciplina cadastrada com este código.';
      }
    } else {
      this.error = 'Erro ao salvar disciplina: ' + (err.error?.message || err.message || JSON.stringify(err));
    }
    this.loading = false;
  }

  navigateBack(): void {
    this.router.navigate(['/coordinator/disciplines']);
  }

  // Métodos auxiliares para validação
  get nameControl() { return this.disciplineForm.get('name'); }
  get codeControl() { return this.disciplineForm.get('code'); }
  get workloadControl() { return this.disciplineForm.get('workload'); }
  get creditsControl() { return this.disciplineForm.get('credits'); }
}
