import { Component, OnInit, HostListener } from '@angular/core';
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
  dropdownOpen = false;
  mobileMenuOpen = false;

  constructor(private authService: AuthService) {}

  async ngOnInit() {
    if (await this.authService.isAuthenticated()) {
      this.userProfile = await this.authService.getUserProfile();
      this.userInfo = this.authService.getUserInfo();
      this.userRoles = this.authService.getUserRoles();

      const systemRoles = ['ADMIN', 'COORDENADOR', 'PROFESSOR', 'ALUNO'];
      this.filteredRoles = this.userRoles.filter(role => systemRoles.includes(role));
      console.log('userProfile:', this.userProfile);
      console.log('userInfo:', this.userInfo);
      console.log('userRoles:', this.userRoles);
    } else {
      console.log('Usuário não autenticado');
    }
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  @HostListener('document:click', ['$event'])
  closeDropdown(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const dropdown = document.querySelector('.user-dropdown');
    if (dropdown && !dropdown.contains(target)) {
      this.dropdownOpen = false;
    }
    // Fecha o menu lateral mobile se clicar fora dele
    const mobileMenu = document.querySelector('.mobile-panel');
    const menuButton = document.querySelector('.burger-menu');
    if (
      this.mobileMenuOpen &&
      mobileMenu &&
      !mobileMenu.contains(target) &&
      (!menuButton || !menuButton.contains(target))
    ) {
      this.mobileMenuOpen = false;
    }
  }

  @HostListener('window:resize')
  onResize() {
    if (window.innerWidth > 992 && this.mobileMenuOpen) {
      this.mobileMenuOpen = false;
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
