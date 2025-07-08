import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { AcademicService } from '../../services/academic.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userRoles: string[] = [];
  userName: string = '';
  testingConnection = false;
  connectionResults: any = {};

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private academicService: AcademicService,
    private router: Router
  ) { }

  async ngOnInit() {
    this.userRoles = this.authService.getUserRoles();

    try {
      const userProfile = await this.authService.getUserProfile();
      this.userName = userProfile.firstName || userProfile.username || 'Usuário';
    } catch (error) {
      this.userName = 'Usuário';
    }
  }

  navigateToAdminPanel() {
    this.router.navigate(['/admin']);
  }

  navigateToCoordinatorPanel() {
    this.router.navigate(['/coordinator']);
  }

  navigateToProfessorPanel() {
    this.router.navigate(['/professor']);
  }

  navigateToStudentPanel() {
    this.router.navigate(['/student']);
  }

  navigateToViewCurriculums() {
    this.router.navigate(['/view/curriculums']);
  }

  navigateToViewPanel() {
    this.router.navigate(['/view']);
  }

  hasRole(role: string): boolean {
    return this.userRoles.includes(role);
  }

  hasAdminRole(): boolean {
    return this.userRoles.some(role => role.toUpperCase() === 'ADMIN');
  }

  hasCoordinatorRole(): boolean {
    return this.userRoles.some(role => role.toUpperCase() === 'COORDENADOR');
  }

  hasTeacherRole(): boolean {
    return this.userRoles.some(role => role.toUpperCase() === 'PROFESSOR');
  }

  hasStudentRole(): boolean {
    return this.userRoles.some(role => role.toUpperCase() === 'ALUNO');
  }

  hasTeacherOrStudentRole(): boolean {
    return this.hasTeacherRole() || this.hasStudentRole();
  }

  async testBackendConnection() {
    this.testingConnection = true;
    this.connectionResults = {};

    try {
      const healthUrl = `${environment.backend.url}/q/health`;
      const healthResponse = await fetch(healthUrl);

      if (healthResponse.ok) {
        const healthData = await healthResponse.json();
        this.connectionResults.health = { success: true, data: 'Backend está respondendo' };
      } else {
        this.connectionResults.health = { success: false, error: { message: `Health check falhou: ${healthResponse.status}` } };
      }
    } catch (error) {
      this.connectionResults.health = { success: false, error: { message: 'Backend não está respondendo' } };
    }

    try {
      const token = await this.authService.getToken();

      if (!token) {
        this.connectionResults.auth = { success: false, error: { message: 'Token de autenticação não encontrado' } };
      } else {
        this.connectionResults.auth = { success: true, data: 'Token presente' };
      }
    } catch (error) {
      this.connectionResults.auth = { success: false, error: error };
    }

    try {
      this.userService.getUsers().subscribe({
        next: (users) => {
          this.connectionResults.users = { success: true, data: users };
        },
        error: (error) => {
          this.connectionResults.users = { success: false, error: error };
        }
      });
    } catch (error) {
      this.connectionResults.users = { success: false, error: error };
    }

    try {
      this.academicService.getCourses().subscribe({
        next: (courses) => {
          this.connectionResults.courses = { success: true, data: courses };
        },
        error: (error) => {
          this.connectionResults.courses = { success: false, error: error };
        }
      });
    } catch (error) {
      this.connectionResults.courses = { success: false, error: error };
    }

    try {
      this.academicService.getDisciplines().subscribe({
        next: (disciplines) => {
          this.connectionResults.disciplines = { success: true, data: disciplines };
          this.testingConnection = false;
        },
        error: (error) => {
          this.connectionResults.disciplines = { success: false, error: error };
          this.testingConnection = false;
        }
      });
    } catch (error) {
      this.connectionResults.disciplines = { success: false, error: error };
      this.testingConnection = false;
    }
  }

  getObjectKeys(obj: any): string[] {
    return Object.keys(obj);
  }

  getObjectEntries(obj: any): [string, any][] {
    return Object.entries(obj);
  }

  getPrimaryRole(): string {
    const rolePriority = ['ADMIN', 'COORDENADOR', 'PROFESSOR', 'ALUNO'];

    const filteredRoles = this.userRoles.filter(role =>
      rolePriority.includes(role)
    );

    for (const role of rolePriority) {
      if (filteredRoles.includes(role)) {
        return role;
      }
    }

    return '';
  }
}
