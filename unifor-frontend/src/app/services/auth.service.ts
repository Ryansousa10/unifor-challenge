import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private keycloakService: KeycloakService) {}

  /**
   * Verifica se o usuário está autenticado
   */
  async isAuthenticated(): Promise<boolean> {
    return await this.keycloakService.isLoggedIn();
  }

  /**
   * Obtém o perfil do usuário
   */
  async getUserProfile() {
    return await this.keycloakService.loadUserProfile();
  }

  /**
   * Obtém as roles do usuário
   */
  getUserRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }

  /**
   * Verifica se o usuário tem uma role específica
   */
  hasRole(role: string): boolean {
    return this.keycloakService.isUserInRole(role);
  }

  /**
   * Verifica se o usuário tem pelo menos uma das roles especificadas
   */
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  /**
   * Verifica se o usuário tem todas as roles especificadas
   */
  hasAllRoles(roles: string[]): boolean {
    return roles.every(role => this.hasRole(role));
  }

  /**
   * Obtém o token de acesso
   */
  async getToken(): Promise<string> {
    return await this.keycloakService.getToken();
  }

  /**
   * Faz logout do usuário
   */
  async logout(): Promise<void> {
    await this.keycloakService.logout(window.location.origin);
  }

  /**
   * Obtém informações básicas do usuário
   */
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

  /**
   * Verifica se o usuário é admin
   */
  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  /**
   * Verifica se o usuário é coordenador
   */
  isCoordenador(): boolean {
    return this.hasRole('COORDENADOR');
  }

  /**
   * Verifica se o usuário é professor
   */
  isProfessor(): boolean {
    return this.hasRole('PROFESSOR');
  }

  /**
   * Verifica se o usuário é aluno
   */
  isAluno(): boolean {
    return this.hasRole('ALUNO');
  }
}
