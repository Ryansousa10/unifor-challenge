import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';
import { AcademicService } from '../../../../services/academic.service';
import { Curriculum } from '../../../../models/curriculum.model';

@Component({
  selector: 'app-curriculum-form',
  templateUrl: './curriculum-form.component.html',
  styleUrls: ['./curriculum-form.component.css']
})
export class CurriculumFormComponent implements OnInit {
  curriculumForm: FormGroup;
  isEditMode = false;
  curriculumId: string | null = null;
  loading = false;
  submitted = false;
  error = '';
  courses: any[] = [];
  semesters: any[] = [];
  disciplines: any[] = [];
  selectedDisciplines: any[] = [];
  curriculums: Curriculum[] = [];

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
      semesterId: ['', Validators.required],
      active: [true],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadCourses();
    this.loadSemesters();
    this.loadDisciplines();
    this.loadCurriculums();

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

  loadSemesters(): void {
    this.academicService.getSemesters().subscribe({
      next: (data) => {
        this.semesters = data;
      },
      error: (err) => {
        this.error = 'Erro ao carregar semestres. Por favor, tente novamente.';
        console.error('Erro ao carregar semestres:', err);
      }
    });
  }

  loadDisciplines(): void {
    this.academicService.getDisciplines().subscribe({
      next: (data) => {
        this.disciplines = data;
      },
      error: (err) => {
        this.error = 'Erro ao carregar disciplinas. Por favor, tente novamente.';
        console.error('Erro ao carregar disciplinas:', err);
      }
    });
  }

  loadCurriculums(): void {
    this.curriculumService.getCurriculums().subscribe({
      next: (data) => {
        // Garante que cada curriculum tenha os campos esperados
        this.curriculums = (data as any[]).map(item => ({
          id: item.id,
          courseId: item.courseId,
          semesterId: item.semesterId,
          name: item.name ?? '',
          description: item.description ?? '',
          active: item.active ?? false,
          disciplines: item.disciplines ?? []
        }));
      },
      error: () => this.curriculums = []
    });
  }

  loadCurriculum(id: string): void {
    this.loading = true;
    this.curriculumService.getCurriculum(id).subscribe({
      next: (data: any) => {
        this.curriculumForm.patchValue({
          name: data.name,
          courseId: data.courseId,
          semesterId: data.semesterId || (data.semester && data.semester.id) || '',
          active: data.active,
          description: data.description
        });
        // Monta selectedDisciplines com nome e ordena pelo ordering
        let disciplinesArr: any[] = [];
        if (Array.isArray(data.disciplines)) {
          disciplinesArr = data.disciplines;
        } else if (data.disciplines && typeof data.disciplines === 'object') {
          disciplinesArr = Object.values(data.disciplines);
        } else if (data.disciplines) {
          disciplinesArr = [data.disciplines];
        } else if (data.curriculum && Array.isArray(data.curriculum.disciplines)) {
          disciplinesArr = data.curriculum.disciplines;
        }
        this.selectedDisciplines = disciplinesArr
          .map((d: any) => {
            const discId = d.disciplineId || d.id;
            const disc = this.disciplines.find((x: any) => x.id === discId);
            return {
              id: discId,
              name: disc ? disc.name : '(Disciplina não encontrada)',
              ordering: d.ordering
            };
          })
          .sort((a, b) => a.ordering - b.ordering);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar matriz curricular. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao carregar matriz curricular:', err);
      }
    });
  }

  addDiscipline(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const disciplineId = target?.value;
    if (!disciplineId) return;
    const discipline = this.disciplines.find(d => d.id === disciplineId);
    if (discipline && !this.selectedDisciplines.find(d => d.id === discipline.id)) {
      this.selectedDisciplines.push({ ...discipline, ordering: this.selectedDisciplines.length + 1 });
    }
    // Resetar o select após adicionar
    if (target) target.value = '';
  }

  removeDiscipline(index: number): void {
    this.selectedDisciplines.splice(index, 1);
    this.updateOrdering();
  }

  moveDisciplineUp(index: number): void {
    if (index > 0) {
      [this.selectedDisciplines[index - 1], this.selectedDisciplines[index]] = [this.selectedDisciplines[index], this.selectedDisciplines[index - 1]];
      this.updateOrdering();
    }
  }

  moveDisciplineDown(index: number): void {
    if (index < this.selectedDisciplines.length - 1) {
      [this.selectedDisciplines[index + 1], this.selectedDisciplines[index]] = [this.selectedDisciplines[index], this.selectedDisciplines[index + 1]];
      this.updateOrdering();
    }
  }

  updateOrdering(): void {
    this.selectedDisciplines.forEach((d, i) => d.ordering = i + 1);
  }

  get f() { return this.curriculumForm.controls; }

  async onSubmit() {
    this.submitted = true;

    if (this.curriculumForm.invalid || this.selectedDisciplines.length === 0) {
      this.error = 'Preencha todos os campos obrigatórios e adicione pelo menos uma disciplina.';
      return;
    }

    // Validação de duplicidade no frontend
    const formValue = this.curriculumForm.value;
    const isDuplicate = this.curriculums.some(c =>
      c.courseId === formValue.courseId &&
      c.semesterId === formValue.semesterId &&
      (!this.isEditMode || c.id !== this.curriculumId)
    );
    if (isDuplicate) {
      this.error = 'Já existe uma matriz curricular para este curso e semestre.';
      return;
    }

    this.loading = true;
    this.error = '';

    const payload = {
      ...formValue,
      disciplines: this.selectedDisciplines.map((d: any) => ({
        disciplineId: d.id,
        ordering: d.ordering
      }))
    };

    if (this.isEditMode && this.curriculumId) {
      this.updateCurriculum(payload);
    } else {
      this.createCurriculum(payload);
    }
  }

  createCurriculum(payload: any): void {
    this.curriculumService.createCurriculum(payload).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/coordinator/curriculums']);
      },
      error: (err) => {
        if (err?.error?.message?.includes('matriz curricular para este curso e semestre')) {
          this.error = 'Já existe uma matriz curricular para este curso e semestre.';
        } else {
          this.error = 'Erro ao criar matriz curricular. Por favor, tente novamente.';
        }
        this.loading = false;
        console.error('Erro ao criar matriz curricular:', err);
      }
    });
  }

  updateCurriculum(payload: any): void {
    if (!this.curriculumId) return;

    this.curriculumService.updateCurriculum(this.curriculumId, payload).subscribe({
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
