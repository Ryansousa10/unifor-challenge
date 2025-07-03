# Sistema de Gestão Acadêmica - UNIFOR Backend

Este projeto faz parte do desafio de desenvolvimento para a UNIFOR. O objetivo é criar uma aplicação web responsiva para administração de alunos, professores e cursos, com backend em Java (Quarkus) e frontend em Angular 15+ (standalone), seguindo as instruções do desafio.

## 🎯 Escopo do Desafio

- **Visão de Administrador:** Incluir, excluir, atualizar e visualizar usuários.
- **Visão de Coordenador:** Incluir, excluir, atualizar e visualizar semestres, cursos e disciplinas; montar a matriz curricular.
- **Visão de Professor e Aluno:** Visualizar a matriz curricular.
- **Segurança:** Políticas de acesso gerenciadas pelo Keycloak.
- **Separação:** Backend (Java/Quarkus) e frontend (Angular) desenvolvidos separadamente.
- **Banco de Dados:** Relacional (PostgreSQL).
- **Orquestração:** Docker e docker-compose.

## 📄 Suposições e Decisões Técnicas

- O backend expõe uma API REST documentada via OpenAPI (Swagger).
- O frontend (Angular) consome a API REST e implementa as visões conforme o perfil do usuário.
- O controle de acesso é feito via Keycloak, com papéis: ADMIN, COORDENADOR, PROFESSOR, ALUNO.
- O banco de dados utilizado é PostgreSQL, mas pode ser facilmente adaptado para outros bancos relacionais.
- O projeto utiliza Flyway para versionamento do banco.
- Testes unitários são implementados com JUnit 5.
- O histórico de commits é mantido limpo e descritivo.
- O backend está preparado para ser executado em ambiente Docker.
- Todas as decisões técnicas, bibliotecas e patterns utilizados estão documentados neste README e nos comentários do código.

## 🚀 Tecnologias

- Java 17
- Quarkus 3.24.1
- PostgreSQL
- Keycloak
- Docker
- Flyway (Migrations)
- JUnit 5 (Testes)

## 📋 Requisitos

- Java 17 ou superior
- Maven
- Docker e Docker Compose
- PostgreSQL (opcional se usar Docker)

## 🔧 Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/Ryansousa10/unifor-challenge-backend.git
cd unifor-challenge-backend
```

2. Inicie a aplicação e todos os serviços necessários com Docker Compose:
```bash
docker-compose up -d
```

A aplicação estará disponível para requisições em http://localhost:8080

## 🗄️ Estrutura do Banco de Dados

O sistema utiliza migrations (Flyway) para gerenciar a estrutura do banco de dados:

- V1: Usuários e Papéis (users, role, user_role)
- V2: Entidades Acadêmicas (course, semester, discipline)
- V3: Estrutura Curricular (curriculum, curric_disc)
- V4: Matrículas (enrollment)

## 🔐 Autenticação e Papéis

O sistema utiliza Keycloak para autenticação e autorização, com os seguintes papéis:

- ADMIN
- COORDENADOR
- PROFESSOR
- ALUNO

### Permissões por Papel

| Recurso      | ADMIN | COORDENADOR | PROFESSOR | ALUNO |
|--------------|:-----:|:-----------:|:---------:|:-----:|
| Usuários     | CRUD  |     R       |     R     |   R   |
| Cursos       | CRUD  |    CRUD     |     R     |   R   |
| Disciplinas  | CRUD  |    CRUD     |     R     |   R   |
| Currículos   | CRUD  |    CRUD     |     R     |   R   |
| Semestres    | CRUD  |    CRUD     |     R     |   R   |

CRUD = Criar, Listar, Atualizar, Remover; R = Listar/Visualizar

## 🌐 Endpoints da API (REST)

Os endpoints seguem o padrão REST e estão documentados no arquivo `src/main/resources/openapi.yaml`.

### Usuários
- GET    /users           - Lista todos os usuários
- POST   /users           - Cria novo usuário
- GET    /users/{id}      - Obtém usuário específico
- PUT    /users/{id}      - Atualiza usuário
- DELETE /users/{id}      - Remove usuário

### Cursos
- GET    /course           - Lista todos os cursos
- POST   /course           - Cria novo curso
- GET    /course/{id}      - Obtém curso específico
- PUT    /course/{id}      - Atualiza curso
- DELETE /course/{id}      - Remove curso

### Disciplinas
- GET    /discipline           - Lista todas as disciplinas
- POST   /discipline           - Cria nova disciplina
- GET    /discipline/{id}      - Obtém disciplina específica
- PUT    /discipline/{id}      - Atualiza disciplina
- DELETE /discipline/{id}      - Remove disciplina

### Currículos
- GET    /curriculum           - Lista todos os currículos
- POST   /curriculum           - Cria novo currículo
- GET    /curriculum/{id}      - Obtém currículo específico
- PUT    /curriculum/{id}      - Atualiza currículo
- DELETE /curriculum/{id}      - Remove currículo

### Semestres
- GET    /semester           - Lista todos os semestres
- POST   /semester           - Cria novo semestre
- GET    /semester/{id}      - Obtém semestre específico
- PUT    /semester/{id}      - Atualiza semestre
- DELETE /semester/{id}      - Remove semestre

## 📄 Exemplos de Uso

### Criar Usuário
```json
POST /users
{
  "username": "maria.silva",
  "password": "senhaSegura123",
  "firstName": "Maria",
  "lastName": "Silva",
  "email": "maria.silva@unifor.br",
  "roles": ["550e8400-e29b-41d4-a716-446655440000"]
}
```

### Criar Curso
```json
POST /course
{
  "name": "Engenharia de Computação",
  "code": "ENGC",
  "description": "Curso de Engenharia de Computação"
}
```

### Criar Disciplina
```json
POST /discipline
{
  "name": "Algoritmos",
  "code": "CS102",
  "credits": 4,
  "semester": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Criar Currículo
```json
POST /curriculum
{
  "courseId": "123e4567-e89b-12d3-a456-426614174000",
  "semesterId": "123e4567-e89b-12d3-a456-426614174001"
}
```

### Criar Semestre
```json
POST /semester
{
  "name": "2024.1",
  "startDate": "2024-01-15",
  "endDate": "2024-06-30"
}
```

## 📚 Documentação OpenAPI

A documentação completa da API está disponível no arquivo `src/main/resources/openapi.yaml` e pode ser visualizada em ferramentas como Swagger Editor ou Insomnia.

---

Caso encontre divergências entre o README, o código e a documentação OpenAPI, priorize sempre o arquivo openapi.yaml e os endpoints REST descritos acima.
