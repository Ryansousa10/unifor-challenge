import { Component, OnInit } from '@angular/core';
import { CurriculumService } from 'src/app/services/curriculum.service';
import { Router } from '@angular/router';
import { AcademicService } from 'src/app/services/academic.service';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
  curriculums: any[] = [];
  filteredCurriculums: any[] = [];
  loading = true;
  error = '';
  search = '';
  courses: any[] = [];
  semesters: any[] = [];
  disciplines: any[] = [];

  constructor(
    private curriculumService: CurriculumService,
    private academicService: AcademicService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loading = true;
    Promise.all([
      this.academicService.getCourses().toPromise(),
      this.academicService.getSemesters().toPromise(),
      this.academicService.getDisciplines().toPromise(),
      this.curriculumService.getCurriculums().toPromise()
    ]).then(([courses, semesters, disciplines, curriculums]: any) => {
      this.courses = courses;
      this.semesters = semesters;
      this.disciplines = disciplines;
      // Monta lista de currículos com nomes e quantidade de disciplinas
      this.curriculums = curriculums.map((c: any) => {
        const course = this.courses.find((x: any) => x.id === c.courseId);
        const semester = this.semesters.find((x: any) => x.id === c.semesterId);
        let disciplineCount = 0;
        if (Array.isArray(c.disciplines)) {
          disciplineCount = c.disciplines.length;
        } else if (c.disciplines && typeof c.disciplines === 'object') {
          disciplineCount = Object.keys(c.disciplines).length;
        }
        return {
          ...c,
          courseName: course ? course.name : '-',
          semesterName: semester ? semester.name : '-',
          disciplineCount
        };
      });
      this.filteredCurriculums = this.curriculums;
      this.loading = false;
    }).catch(() => {
      this.error = 'Erro ao carregar dados das matrizes curriculares.';
      this.loading = false;
    });
  }

  onSearch() {
    const term = this.search.trim().toLowerCase();
    if (!term) {
      this.filteredCurriculums = this.curriculums;
      return;
    }
    this.filteredCurriculums = this.curriculums.filter(c =>
      c.name.toLowerCase().includes(term) ||
      (c.courseName?.toLowerCase().includes(term) ?? false) ||
      (c.semesterName?.toLowerCase().includes(term) ?? false)
    );
  }

  goToDetails(curriculum: any) {
    this.router.navigate(['/view/curriculums', curriculum.id]);
  }
}
