import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Course } from '../../../../models/academic.model';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {
  courses: Course[] = [];
  loading = false;
  error = '';

  constructor(
    private academicService: AcademicService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.loading = true;
    this.error = '';

    this.academicService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Erro ao carregar cursos. Tente novamente mais tarde.';
        this.loading = false;
      }
    });
  }

  navigateToNewCourse(): void {
    this.router.navigate(['/coordinator/courses/new']);
  }

  editCourse(id: string): void {
    this.router.navigate([`/coordinator/courses/edit/${id}`]);
  }

  deleteCourse(id: string): void {
    if (confirm('Tem certeza que deseja excluir este curso?')) {
      this.academicService.deleteCourse(id).subscribe({
        next: () => {
          this.loadCourses();
        },
        error: () => {
          this.error = 'Erro ao excluir curso. Tente novamente mais tarde.';
        }
      });
    }
  }
}
