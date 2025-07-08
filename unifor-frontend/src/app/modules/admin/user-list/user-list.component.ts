import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserResponse } from '../../../models/user.model';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: UserResponse[] = [];
  loading = false;
  error = '';

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.error = '';

    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar usuários. Tente novamente mais tarde.';
        console.error('Erro ao buscar usuários:', err);
        this.loading = false;
      }
    });
  }

  navigateToNewUser(): void {
    this.router.navigate(['/admin/users/new']);
  }

  editUser(id: string): void {
    this.router.navigate([`/admin/users/edit/${id}`]);
  }

  deleteUser(id: string): void {
    if (confirm('Tem certeza que deseja excluir este usuário?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.loadUsers();
        },
        error: (err) => {
          this.error = 'Erro ao excluir usuário. Tente novamente mais tarde.';
          console.error('Erro ao excluir usuário:', err);
        }
      });
    }
  }
}
