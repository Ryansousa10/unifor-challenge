import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Discipline } from '../../../../models/academic.model';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-discipline-list',
  templateUrl: './discipline-list.component.html',
  styleUrls: ['./discipline-list.component.css']
})
export class DisciplineListComponent implements OnInit {
  disciplines: Discipline[] = [];
  loading = false;
  error = '';

  constructor(
    private academicService: AcademicService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadDisciplines();
  }

  loadDisciplines(): void {
    this.loading = true;
    this.error = '';

    this.academicService.getDisciplines().subscribe({
      next: (data) => {
        this.disciplines = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar disciplinas. Tente novamente mais tarde.';
        console.error('Erro ao buscar disciplinas:', err);
        this.loading = false;
      }
    });
  }

  navigateToNewDiscipline(): void {
    this.router.navigate(['/coordinator/disciplines/new']);
  }

  editDiscipline(id: string): void {
    this.router.navigate([`/coordinator/disciplines/edit/${id}`]);
  }

  deleteDiscipline(id: string): void {
    if (confirm('Tem certeza que deseja excluir esta disciplina?')) {
      this.academicService.deleteDiscipline(id).subscribe({
        next: () => {
          this.loadDisciplines();
        },
        error: (err) => {
          this.error = 'Erro ao excluir disciplina. Tente novamente mais tarde.';
          console.error('Erro ao excluir disciplina:', err);
        }
      });
    }
  }
}
