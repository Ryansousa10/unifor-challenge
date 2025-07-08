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
  private getApiUrl() {
    return `${environment.backend.url}${environment.backend.apiPath}`;
  }

  constructor(private http: HttpClient) { }

  getCurriculums(): Observable<Curriculum[]> {
    return this.http.get<Curriculum[]>(`${this.getApiUrl()}/curriculums`);
  }

  getCurriculum(id: string): Observable<Curriculum> {
    return this.http.get<Curriculum>(`${this.getApiUrl()}/curriculums/${id}`);
  }

  getCurriculumWithDisciplines(id: string): Observable<CurriculumWithDisciplines> {
    return this.http.get<CurriculumWithDisciplines>(`${this.getApiUrl()}/curriculums/${id}/disciplines`);
  }

  createCurriculum(curriculum: Curriculum): Observable<Curriculum> {
    return this.http.post<Curriculum>(`${this.getApiUrl()}/curriculums`, curriculum);
  }

  updateCurriculum(id: string, curriculum: Curriculum): Observable<Curriculum> {
    return this.http.put<Curriculum>(`${this.getApiUrl()}/curriculums/${id}`, curriculum);
  }

  deleteCurriculum(id: string): Observable<void> {
    return this.http.delete<void>(`${this.getApiUrl()}/curriculums/${id}`);
  }

  addDisciplineToCurriculum(curriculumId: string, disciplineId: string, data: CurricDisc): Observable<any> {
    return this.http.post(`${this.getApiUrl()}/curriculums/${curriculumId}/disciplines/${disciplineId}`, data);
  }

  removeDisciplineFromCurriculum(curriculumId: string, disciplineId: string): Observable<void> {
    return this.http.delete<void>(`${this.getApiUrl()}/curriculums/${curriculumId}/disciplines/${disciplineId}`);
  }
}
