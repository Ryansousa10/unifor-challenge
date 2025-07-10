import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AcademicService} from '../../../../services/academic.service';

@Component({
    selector: 'app-semester-list',
    templateUrl: './semester-list.component.html',
    styleUrls: ['./semester-list.component.css']
})
export class SemesterListComponent implements OnInit {
    semesters: any[] = [];
    loading = false;

    constructor(
        private academicService: AcademicService,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.fetchSemesters();
    }

    fetchSemesters(): void {
        this.loading = true;
        this.academicService.getSemesters().subscribe({
            next: (data) => {
                this.semesters = data;
                this.loading = false;
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    navigateToNewSemester(): void {
        this.router.navigate(['coordinator/semesters/new']);
    }

    editSemester(id: string): void {
        this.router.navigate(['coordinator/semesters/edit', id]);
    }

    deleteSemester(id: string): void {
        if (confirm('Tem certeza que deseja excluir este semestre?')) {
            this.loading = true;
            this.academicService.deleteSemester(id).subscribe({
                next: () => {
                    this.fetchSemesters();
                },
                error: () => {
                    this.loading = false;
                }
            });
        }
    }
}
