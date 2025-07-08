import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Unifor - Sistema de Gestão';
  userProfile: any = null;
  userInfo: any = null;
  userRoles: string[] = [];
  filteredRoles: string[] = [];

  constructor(private authService: AuthService) {}

  async ngOnInit() {
    if (await this.authService.isAuthenticated()) {
      this.userProfile = await this.authService.getUserProfile();
      this.userInfo = this.authService.getUserInfo();
      this.userRoles = this.authService.getUserRoles();

      const systemRoles = ['ADMIN', 'COORDENADOR', 'PROFESSOR', 'ALUNO'];
      this.filteredRoles = this.userRoles.filter(role => systemRoles.includes(role));
    }
  }

  async logout() {
    await this.authService.logout();
  }

  hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  get username(): string {
    return this.userInfo?.name || this.userInfo?.username || 'Usuário';
  }

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  get isCoordenador(): boolean {
    return this.authService.isCoordenador();
  }

  get isProfessor(): boolean {
    return this.authService.isProfessor();
  }

  get isAluno(): boolean {
    return this.authService.isAluno();
  }
}
