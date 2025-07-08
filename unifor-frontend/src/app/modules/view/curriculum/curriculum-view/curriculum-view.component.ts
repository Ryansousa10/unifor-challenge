import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CurriculumWithDisciplines } from '../../../../models/curriculum.model';
import { CurriculumService } from '../../../../services/curriculum.service';

@Component({
  selector: 'app-curriculum-view',
  templateUrl: './curriculum-view.component.html',
  styleUrls: ['./curriculum-view.component.css']
})
export class CurriculumViewComponent implements OnInit {
  curriculumWithDisciplines?: CurriculumWithDisciplines;
  loading = false;
  error = '';
  semesters: number[] = [];

  constructor(
    private curriculumService: CurriculumService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadCurriculumWithDisciplines(+id);
    }
  }

  loadCurriculumWithDisciplines(id: number): void {
    this.loading = true;
    this.error = '';

    this.curriculumService.getCurriculumWithDisciplines(id).subscribe({
      next: (data) => {
        this.curriculumWithDisciplines = data;
        this.organizeSemesters();
        this.loading = false;
      },
      error: () => {
        this.error = 'Erro ao carregar detalhes da matriz curricular. Tente novamente mais tarde.';
        this.loading = false;
      }
    });
  }

  organizeSemesters(): void {
    if (this.curriculumWithDisciplines?.disciplines) {
      const semestersSet = new Set<number>();
      this.curriculumWithDisciplines.disciplines.forEach(disc => {
        semestersSet.add(disc.semester);
      });
      this.semesters = Array.from(semestersSet).sort((a, b) => a - b);
    }
  }

  getDisciplinesBySemester(semester: number): any[] {
    return this.curriculumWithDisciplines?.disciplines.filter(disc => disc.semester === semester) || [];
  }

  navigateBack(): void {
    this.router.navigate(['/view/curriculums']);
  }
}
