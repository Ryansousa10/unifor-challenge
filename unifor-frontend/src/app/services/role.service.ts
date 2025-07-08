import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Role } from '../models/user.model';
import { environment } from '../../environments/environment';

interface RoleBackendResponse {
  id: string;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiUrl = `${environment.backend.url}${environment.backend.apiPath}/roles`;

  constructor(private http: HttpClient) {}

  getRoles(): Observable<Role[]> {
    return this.http.get<RoleBackendResponse[]>(this.apiUrl).pipe(
      map(backendRoles => backendRoles.map(role => this.mapToFrontendRole(role))),
      catchError(this.handleError)
    );
  }

  private mapToFrontendRole(backendRole: RoleBackendResponse): Role {
    const descriptions: { [key: string]: string } = {
      'ADMIN': 'Administrador',
      'COORDENADOR': 'Coordenador',
      'PROFESSOR': 'Professor',
      'ALUNO': 'Aluno'
    };

    return {
      id: backendRole.id,
      name: backendRole.name,
      description: descriptions[backendRole.name] || backendRole.name
    };
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro na requisição';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro do cliente: ${error.error.message}`;
    } else {
      errorMessage = `Erro do servidor: ${error.status}, mensagem: ${error.message}`;
    }

    return throwError(() => new Error(errorMessage));
  }
}
