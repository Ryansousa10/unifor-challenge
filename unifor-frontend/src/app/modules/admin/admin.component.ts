import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  template: `
    <div class="admin-container">
      <header class="admin-header">
        <h1>Painel de Administração</h1>
        <nav class="admin-nav">
          <a routerLink="/dashboard" class="nav-link">Dashboard</a>
          <a routerLink="/admin/users" class="nav-link active">Usuários</a>
        </nav>
      </header>
      <main class="admin-content">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .admin-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }
    
    .admin-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      padding-bottom: 10px;
      border-bottom: 1px solid #e0e0e0;
    }
    
    .admin-header h1 {
      margin: 0;
      color: #333;
    }
    
    .admin-nav {
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
    
    .admin-content {
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      padding: 20px;
    }
    
    @media (max-width: 768px) {
      .admin-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }
      
      .admin-nav {
        width: 100%;
        overflow-x: auto;
        padding-bottom: 5px;
      }
    }
  `]
})
export class AdminComponent {
}
