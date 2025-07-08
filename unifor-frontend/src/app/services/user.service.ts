import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { User, UserRequest, UserResponse } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private getApiUrl() {
    return `${environment.backend.url}${environment.backend.apiPath}`;
  }

  constructor(private http: HttpClient) {}

  getUsers(): Observable<UserResponse[]> {
    console.log('Buscando usuários em:', `${this.getApiUrl()}/users`);
    return this.http.get<UserResponse[]>(`${this.getApiUrl()}/users`).pipe(
      tap(users => console.log('Usuários encontrados:', users)),
      catchError(this.handleError)
    );
  }

  getUser(id: string): Observable<User> {
    return this.http.get<User>(`${this.getApiUrl()}/users/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createUser(userRequest: UserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.getApiUrl()}/users`, userRequest).pipe(
      catchError(this.handleError)
    );
  }

  updateUser(id: string, userUpdate: UserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.getApiUrl()}/users/${id}`, userUpdate).pipe(
      catchError(this.handleError)
    );
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.getApiUrl()}/users/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  async getCurrentUser(): Promise<User | undefined> {
    try {
      return {
        id: 'current-user',
        username: 'current-user',
        firstName: 'Usuário',
        lastName: 'Atual',
        email: 'user@example.com',
        roles: []
      };
    } catch (error) {
      console.error('Erro ao obter usuário atual:', error);
      return undefined;
    }
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Erro desconhecido';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      errorMessage = `Código do erro: ${error.status}\nMensagem: ${error.message}`;
    }

    console.error('Erro na requisição HTTP:', error);
    return throwError(() => new Error(errorMessage));
  }
}
