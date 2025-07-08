export interface User {
  id?: string; // UUID no backend
  username: string;
  password?: string; // Apenas para criação
  firstName: string;
  lastName: string;
  email: string;
  createdAt?: string;
  roles?: Role[];
}

export interface UserRequest {
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: string[]; // Array de UUIDs dos roles
}

export interface UserResponse {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  createdAt: string;
  roles: string[]; // Array com nomes dos roles
}

export interface Role {
  id: string; // UUID no backend
  name: string;
  description?: string; // Campo opcional para exibição no frontend
}
