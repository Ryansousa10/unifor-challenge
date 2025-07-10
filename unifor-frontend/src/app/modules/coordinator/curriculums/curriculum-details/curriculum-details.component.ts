import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CurriculumService } from '../../../../services/curriculum.service';
import { AcademicService } from '../../../../services/academic.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-curriculum-details',
  templateUrl: './curriculum-details.component.html',
  styleUrls: ['./curriculum-details.component.css']
})
export class CurriculumDetailsComponent implements OnInit {
  curriculum: any;
  disciplines: any[] = [];
  courseName = '';
  semesterName = '';
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private curriculumService: CurriculumService,
    private academicService: AcademicService,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.fetchCurriculum(id);
    }
  }

  fetchCurriculum(id: string): void {
    this.loading = true;
    this.curriculumService.getCurriculum(id).subscribe({
      next: (data) => {
        this.curriculum = data;
        this.fetchCourseAndSemester();
        this.fetchDisciplines();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar matriz curricular.';
        this.loading = false;
      }
    });
  }

  fetchCourseAndSemester(): void {
    if (this.curriculum?.courseId) {
      this.academicService.getCourse(this.curriculum.courseId).subscribe({
        next: (c) => this.courseName = c.name,
        error: () => this.courseName = '-'
      });
    }
    if (this.curriculum?.semesterId) {
      this.academicService.getSemester(this.curriculum.semesterId).subscribe({
        next: (s) => this.semesterName = s.name,
        error: () => this.semesterName = '-'
      });
    }
  }

  fetchDisciplines(): void {
    if (this.curriculum?.disciplines?.length) {
      this.academicService.getDisciplines().subscribe({
        next: (all) => {
          this.disciplines = this.curriculum.disciplines.map((d: any) => {
            const disc = all.find((x: any) => x.id === (d.disciplineId || d.id));
            return {
              ...d,
              name: disc ? disc.name : '(Disciplina não encontrada)',
              code: disc ? disc.code : '',
              credits: disc ? disc.credits : '',
              workload: disc ? disc.workload : ''
            };
          });
        },
        error: () => this.disciplines = []
      });
    }
  }

  goBack(): void {
    this.location.back();
  }
}
