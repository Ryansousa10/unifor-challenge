import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private keycloakService: KeycloakService) {}

  async isAuthenticated(): Promise<boolean> {
    return await this.keycloakService.isLoggedIn();
  }

  async getUserProfile() {
    return await this.keycloakService.loadUserProfile();
  }

  getUserRoles(): string[] {
    const allRoles = this.keycloakService.getUserRoles();
    const systemRoles = ['ADMIN', 'COORDENADOR', 'PROFESSOR', 'ALUNO'];
    return allRoles.filter(role => systemRoles.includes(role));
  }

  hasRole(role: string): boolean {
    return this.keycloakService.isUserInRole(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  hasAllRoles(roles: string[]): boolean {
    return roles.every(role => this.hasRole(role));
  }

  async getToken(): Promise<string> {
    return await this.keycloakService.getToken();
  }

  async logout(): Promise<void> {
    await this.keycloakService.logout(window.location.origin);
  }

  getUserInfo() {
    const tokenParsed = this.keycloakService.getKeycloakInstance().tokenParsed;
    return {
      username: tokenParsed?.['preferred_username'] || '',
      email: tokenParsed?.['email'] || '',
      name: tokenParsed?.['name'] || '',
      firstName: tokenParsed?.['given_name'] || '',
      lastName: tokenParsed?.['family_name'] || ''
    };
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isCoordenador(): boolean {
    return this.hasRole('COORDENADOR');
  }

  isProfessor(): boolean {
    return this.hasRole('PROFESSOR');
  }

  isAluno(): boolean {
    return this.hasRole('ALUNO');
  }
}
