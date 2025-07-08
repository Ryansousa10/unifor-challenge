import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Curriculum } from '../models/academic.model';
import { CurricDisc, CurriculumWithDisciplines } from '../models/curriculum.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CurriculumService {
  private baseUrl = `${environment.backend.url}${environment.backend.apiPath}`;

  constructor(private http: HttpClient) { }

  getCurriculums(): Observable<Curriculum[]> {
    return this.http.get<Curriculum[]>(`${this.baseUrl}/curriculums`);
  }

  getCurriculum(id: number): Observable<Curriculum> {
    return this.http.get<Curriculum>(`${this.baseUrl}/curriculums/${id}`);
  }

  createCurriculum(curriculum: Curriculum): Observable<Curriculum> {
    return this.http.post<Curriculum>(`${this.baseUrl}/curriculums`, curriculum);
  }

  updateCurriculum(id: number, curriculum: Curriculum): Observable<Curriculum> {
    return this.http.put<Curriculum>(`${this.baseUrl}/curriculums/${id}`, curriculum);
  }

  deleteCurriculum(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/curriculums/${id}`);
  }

  getCurriculumWithDisciplines(curriculumId: number): Observable<CurriculumWithDisciplines> {
    return this.http.get<CurriculumWithDisciplines>(`${this.baseUrl}/curriculums/${curriculumId}/disciplines`);
  }

  addDisciplineToCurriculum(curricDisc: CurricDisc): Observable<CurricDisc> {
    return this.http.post<CurricDisc>(`${this.baseUrl}/curricdiscs`, curricDisc);
  }

  updateDisciplineInCurriculum(curricDisc: CurricDisc): Observable<CurricDisc> {
    const curriculumId = curricDisc.curriculumId || curricDisc.id?.curriculumId;
    const disciplineId = curricDisc.disciplineId || curricDisc.id?.disciplineId;

    return this.http.put<CurricDisc>(
      `${this.baseUrl}/curricdiscs/${curriculumId}/${disciplineId}`,
      curricDisc
    );
  }

  removeDisciplineFromCurriculum(curriculumId: number, disciplineId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/curricdiscs/${curriculumId}/${disciplineId}`);
  }
}
