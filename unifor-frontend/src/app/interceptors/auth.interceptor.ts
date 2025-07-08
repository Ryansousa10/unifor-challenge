import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';
import { switchMap } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private keycloakService: KeycloakService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Adiciona o token de autorização apenas para requisições do backend
    if (req.url.includes('localhost:8081') || req.url.includes('unifor-backend')) {
      return from(this.keycloakService.getToken()).pipe(
        switchMap(token => {
          if (token) {
            console.log('Adicionando token de autenticação à requisição:', req.url);
            const authReq = req.clone({
              setHeaders: {
                Authorization: `Bearer ${token}`
              }
            });
            return next.handle(authReq);
          }
          console.warn('Token não disponível para a requisição:', req.url);
          return next.handle(req);
        })
      );
    }

    return next.handle(req);
  }
}
