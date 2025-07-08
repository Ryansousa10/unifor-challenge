import { Component, OnInit } from '@angular/core';
import { Curriculum } from '../../../models/academic.model';
import { CurriculumService } from '../../../services/curriculum.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-curriculum-list-view',
  templateUrl: './curriculum-list-view.component.html',
  styleUrls: ['./curriculum-list-view.component.css']
})
export class CurriculumListViewComponent implements OnInit {
  curriculums: Curriculum[] = [];
  loading = false;
  error = '';

  constructor(
    private curriculumService: CurriculumService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCurriculums();
  }

  loadCurriculums(): void {
    this.loading = true;
    this.error = '';

    this.curriculumService.getCurriculums().subscribe({
      next: (data) => {
        this.curriculums = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Erro ao carregar matrizes curriculares. Tente novamente mais tarde.';
        this.loading = false;
      }
    });
  }

  viewCurriculum(id: number): void {
    this.router.navigate([`/view/curriculums/${id}`]);
  }
}
