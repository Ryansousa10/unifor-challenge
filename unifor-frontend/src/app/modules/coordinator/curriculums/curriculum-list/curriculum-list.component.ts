import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';
import { AcademicService } from '../../../../services/academic.service';
import { Curriculum } from '../../../../models/curriculum.model';
import { Course, Semester } from '../../../../models/academic.model';

@Component({
  selector: 'app-curriculum-list',
  templateUrl: './curriculum-list.component.html',
  styleUrls: ['./curriculum-list.component.css']
})
export class CurriculumListComponent implements OnInit {
  curriculums: Curriculum[] = [];
  courses: Course[] = [];
  semesters: Semester[] = [];
  loading = false;
  error = '';

  constructor(
    private curriculumService: CurriculumService,
    private academicService: AcademicService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCoursesAndSemesters();
    this.fetchCurriculums();
  }

  loadCoursesAndSemesters(): void {
    this.academicService.getCourses().subscribe({
      next: (data) => this.courses = data,
      error: () => this.courses = []
    });
    this.academicService.getSemesters().subscribe({
      next: (data) => this.semesters = data,
      error: () => this.semesters = []
    });
  }

  fetchCurriculums(): void {
    this.loading = true;
    this.error = '';

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
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar matrizes curriculares. Tente novamente mais tarde.';
        this.loading = false;
        console.error('Erro ao carregar matrizes curriculares:', err);
      }
    });
  }

  deleteCurriculum(id: string): void {
    if (confirm('Tem certeza que deseja excluir esta matriz curricular?')) {
      this.curriculumService.deleteCurriculum(id).subscribe({
        next: () => this.fetchCurriculums(),
        error: () => this.error = 'Erro ao excluir matriz curricular.'
      });
    }
  }

  viewDetails(id: string): void {
    this.router.navigate(['/coordinator/curriculums/details', id]);
  }

  navigateToNew(): void {
    this.router.navigate(['/coordinator/curriculums/new']);
  }

  getCourseName(courseId: string): string {
    return this.courses.find(c => c.id === courseId)?.name || '-';
  }

  getSemesterName(semesterId: string): string {
    return this.semesters.find(s => s.id === semesterId)?.name || '-';
  }
}
