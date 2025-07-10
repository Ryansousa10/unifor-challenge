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
    return this.http.get<Curriculum[]>(`${this.getApiUrl()}/curriculum`);
  }

  getCurriculum(id: string): Observable<Curriculum> {
    return this.http.get<Curriculum>(`${this.getApiUrl()}/curriculum/${id}`);
  }

  getCurriculumWithDisciplines(id: string): Observable<CurriculumWithDisciplines> {
    return this.http.get<CurriculumWithDisciplines>(`${this.getApiUrl()}/curriculum/${id}/disciplines`);
  }

  createCurriculum(curriculum: Curriculum): Observable<Curriculum> {
    return this.http.post<Curriculum>(`${this.getApiUrl()}/curriculum`, curriculum);
  }

  updateCurriculum(id: string, curriculum: Curriculum): Observable<Curriculum> {
    return this.http.put<Curriculum>(`${this.getApiUrl()}/curriculum/${id}`, curriculum);
  }

  deleteCurriculum(id: string): Observable<void> {
    return this.http.delete<void>(`${this.getApiUrl()}/curriculum/${id}`);
  }

  addDisciplineToCurriculum(curriculumId: string, disciplineId: string, data: CurricDisc): Observable<any> {
    return this.http.post(`${this.getApiUrl()}/curriculum/${curriculumId}/disciplines/${disciplineId}`, data);
  }

  removeDisciplineFromCurriculum(curriculumId: string, disciplineId: string): Observable<void> {
    return this.http.delete<void>(`${this.getApiUrl()}/curriculum/${curriculumId}/disciplines/${disciplineId}`);
  }
}
