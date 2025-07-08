import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Course } from '../../../../models/academic.model';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-course-form',
  templateUrl: './course-form.component.html',
  styleUrls: ['./course-form.component.css']
})
export class CourseFormComponent implements OnInit {
  courseForm!: FormGroup;
  isEditMode = false;
  courseId?: string;
  loading = false;
  error = '';
  success = '';
  existingCodes: string[] = [];
  originalCourseData: any = null;

  constructor(
    private fb: FormBuilder,
    private academicService: AcademicService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.academicService.getCourses().subscribe({
      next: (courses) => {
        this.existingCodes = courses.map((c: any) => c.code?.toLowerCase());
        if (this.isEditMode && this.courseId) {
          this.academicService.getCourse(this.courseId).subscribe({
            next: (course) => {
              const idx = this.existingCodes.indexOf(course.code?.toLowerCase());
              if (idx > -1) this.existingCodes.splice(idx, 1);
            }
          });
        }
      },
      error: () => {
        this.existingCodes = [];
      }
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.courseId = id;
      this.loadCourseData(this.courseId);
    }
  }

  initForm(): void {
    this.courseForm = this.fb.group({
      name: ['', [Validators.required]],
      code: ['', [Validators.required]],
      description: ['']
    });
  }

  loadCourseData(id: string): void {
    this.loading = true;
    this.academicService.getCourse(id).subscribe({
      next: (course) => {
        this.courseForm.patchValue({
          name: course.name,
          code: course.code,
          description: course.description
        });
        this.originalCourseData = {
          name: course.name,
          code: course.code,
          description: course.description
        };
        this.loading = false;
      },
      error: () => {
        this.error = 'Erro ao carregar dados do curso.';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.courseForm.invalid) {
      this.courseForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    const course: Course = this.courseForm.value;
    const codeLower = course.code?.toLowerCase();
    if (!this.isEditMode && this.existingCodes.includes(codeLower)) {
      this.error = 'Já existe um curso com esse código.';
      this.loading = false;
      return;
    }

    if (this.isEditMode && this.originalCourseData) {
      const { name, code, description } = this.originalCourseData;
      if (
        name === course.name &&
        code === course.code &&
        (description || '') === (course.description || '')
      ) {
        this.error = 'Altere algum campo para salvar as mudanças.';
        this.loading = false;
        return;
      }
    }

    if (this.isEditMode && this.courseId) {
      this.academicService.updateCourse(this.courseId, course).subscribe({
        next: () => {
          this.success = 'Curso atualizado com sucesso!';
          this.loading = false;
          setTimeout(() => this.navigateBack(), 2000);
        },
        error: () => {
          this.error = 'Erro ao atualizar curso.';
          this.loading = false;
        }
      });
    } else {
      this.academicService.createCourse(course).subscribe({
        next: () => {
          this.success = 'Curso criado com sucesso!';
          this.loading = false;
          setTimeout(() => this.navigateBack(), 2000);
        },
        error: (err) => {
          const errorMsg = (err.error?.message || err.message || JSON.stringify(err.error) || '').toLowerCase();
          if (
            err.status === 409 ||
            errorMsg.includes('existe') ||
            errorMsg.includes('duplic') ||
            errorMsg.includes('unique') ||
            errorMsg.includes('constraint')
          ) {
            if (errorMsg.includes('código') || errorMsg.includes('codigo') || errorMsg.includes('code')) {
              this.error = 'Já existe um curso com esse código.';
            } else if (errorMsg.includes('nome') || errorMsg.includes('name')) {
              this.error = 'Já existe um curso com esse nome.';
            } else {
              this.error = 'Já existe um curso com esse nome ou código.';
            }
          } else {
            this.error = 'Erro ao criar curso.';
          }
          this.loading = false;
        }
      });
    }
  }

  navigateBack(): void {
    this.router.navigate(['/coordinator/courses']);
  }

  get nameControl() { return this.courseForm.get('name'); }
  get codeControl() { return this.courseForm.get('code'); }
}
