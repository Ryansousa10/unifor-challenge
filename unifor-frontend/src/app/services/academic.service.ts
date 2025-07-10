import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Course, Discipline, Semester } from '../models/academic.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AcademicService {
  private baseUrl = `${environment.backend.url}${environment.backend.apiPath}`;

  constructor(private http: HttpClient) {}

  // Serviços para gerenciar Cursos
  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/course`).pipe(
      catchError(this.handleError)
    );
  }

  getCourse(id: string): Observable<Course> {
    return this.http.get<Course>(`${this.baseUrl}/course/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createCourse(course: Course): Observable<Course> {
    return this.http.post<Course>(`${this.baseUrl}/course`, course).pipe(
      catchError(this.handleError)
    );
  }

  updateCourse(id: string, course: Course): Observable<Course> {
    return this.http.put<Course>(`${this.baseUrl}/course/${id}`, course).pipe(
      catchError(this.handleError)
    );
  }

  deleteCourse(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/course/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Serviços para gerenciar Disciplinas
  getDisciplines(): Observable<Discipline[]> {
    return this.http.get<Discipline[]>(`${this.baseUrl}/discipline`).pipe(
      catchError(this.handleError)
    );
  }

  getDiscipline(id: string): Observable<Discipline> {
    return this.http.get<Discipline>(`${this.baseUrl}/discipline/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createDiscipline(discipline: Discipline): Observable<Discipline> {
    return this.http.post<Discipline>(`${this.baseUrl}/discipline`, discipline).pipe(
      catchError(this.handleError)
    );
  }

  updateDiscipline(id: string, discipline: Discipline): Observable<Discipline> {
    return this.http.put<Discipline>(`${this.baseUrl}/discipline/${id}`, discipline).pipe(
      catchError(this.handleError)
    );
  }

  deleteDiscipline(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/discipline/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Serviços para gerenciar Semestres
  getSemesters(): Observable<Semester[]> {
    return this.http.get<Semester[]>(`${this.baseUrl}/semester`).pipe(
      catchError(this.handleError)
    );
  }

  getSemester(id: string): Observable<Semester> {
    return this.http.get<Semester>(`${this.baseUrl}/semester/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createSemester(semester: Semester): Observable<Semester> {
    return this.http.post<Semester>(`${this.baseUrl}/semester`, semester).pipe(
      catchError(this.handleError)
    );
  }

  updateSemester(id: string, semester: Semester): Observable<Semester> {
    return this.http.put<Semester>(`${this.baseUrl}/semester/${id}`, semester).pipe(
      catchError(this.handleError)
    );
  }

  deleteSemester(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/semester/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Tratamento de erros
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro na requisição';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro do cliente: ${error.error.message}`;
    } else {
      errorMessage = `Erro do servidor: ${error.status}, mensagem: ${error.message}`;
    }

    return throwError(errorMessage);
  }
}
