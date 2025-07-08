import { Component } from '@angular/core';

@Component({
  selector: 'app-coordinator',
  template: `
    <div class="coordinator-container">
      <header class="coordinator-header">
        <h1>Painel do Coordenador</h1>
        <nav class="coordinator-nav">
          <a routerLink="/dashboard" class="nav-link">Dashboard</a>
          <a routerLink="/coordinator/courses" class="nav-link" routerLinkActive="active">Cursos</a>
          <a routerLink="/coordinator/disciplines" class="nav-link" routerLinkActive="active">Disciplinas</a>
          <a routerLink="/coordinator/semesters" class="nav-link" routerLinkActive="active">Semestres</a>
          <a routerLink="/coordinator/curriculums" class="nav-link" routerLinkActive="active">Matrizes Curriculares</a>
        </nav>
      </header>
      <main class="coordinator-content">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .coordinator-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }
    
    .coordinator-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      padding-bottom: 10px;
      border-bottom: 1px solid #e0e0e0;
    }
    
    .coordinator-header h1 {
      margin: 0;
      color: #333;
    }
    
    .coordinator-nav {
      display: flex;
      gap: 15px;
    }
    
    .nav-link {
      text-decoration: none;
      color: #666;
      padding: 5px 10px;
      border-radius: 4px;
      transition: background-color 0.3s;
    }
    
    .nav-link:hover, .nav-link.active {
      background-color: #f0f0f0;
      color: #007bff;
    }
    
    .coordinator-content {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      padding: 20px;
    }
    
    @media (max-width: 768px) {
      .coordinator-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }
      
      .coordinator-nav {
        width: 100%;
        overflow-x: auto;
        padding-bottom: 5px;
      }
    }
  `]
})
export class CoordinatorComponent {
}
