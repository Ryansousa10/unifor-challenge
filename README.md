# Sistema de Gestão Acadêmica - UNIFOR Backend

Este é o backend do sistema de gestão acadêmica do desafio UNIFOR, uma aplicação robusta desenvolvida com Quarkus, PostgreSQL e autenticação via Keycloak, oferecendo uma gestão completa de usuários, perfis e entidades acadêmicas.

## 📋 Índice

- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração e Instalação](#-configuração-e-instalação)
- [Execução 100% Docker](#-execução-100-docker)
- [Endpoints da API (REST)](#-endpoints-da-api-rest)
- [Autenticação e Autorização](#-autenticação-e-autorização)
- [Testes](#-testes)
- [Monitoramento](#-monitoramento)
- [Licença](#-licença)

## 🏗 Arquitetura

O sistema é composto por quatro serviços Docker:

* **Keycloak-DB**: PostgreSQL para armazenar dados do Keycloak
* **Keycloak**: Servidor de autenticação e autorização (realm `unifor`)
* **Unifor-DB**: PostgreSQL para dados da aplicação
* **Backend**: Aplicação Quarkus escrita em Java 17
* **Frontend**: Aplicação Angular (não incluída neste repositório, mas mencionada para contexto)

## ✨ Funcionalidades

O sistema oferece gestão completa de:

* **Usuários e Perfis**
  - Administradores
  - Coordenadores
  - Professores
  - Alunos

* **Entidades Acadêmicas**
  - Cursos
  - Semestres letivos
  - Disciplinas
  - Matrizes curriculares

## 🛠 Tecnologias

* **Backend**
  - Java 17
  - Quarkus 3.24.1
  - Hibernate ORM com Panache
  - Camel Quarkus
  - RESTEasy Reactive
  - JUnit 5 (Testes)
  - OpenAPI para documentação
  - Flyway para migrações de banco de dados
  - PostgreSQL como banco de dados
  - Keycloak para autenticação e autorização
  - OpenID Connect (OIDC) para integração com Keycloak
  - Docker e Docker Compose para orquestração
  - Maven para build e dependências
  - Lombok para redução de boilerplate
  - MapStruct para mapeamento de DTOs
  - UUID como identificadores únicos


* **Frontend**
  - Angular 17
  - Angular Material para UI
  - RxJS para programação reativa
  - HttpClient para comunicação com o backend
  - Swagger UI para documentação da API
  - Bootstrap para layout responsivo
  - NPM para gerenciamento de pacotes
  - TypeScript para desenvolvimento
  - Docker para containerização

## 🚀 Pré-requisitos

- **Docker**: Certifique-se de ter o Docker Desktop instalado e em execução.

## ⚙️ Configuração e Instalação

1. Clone o repositório:
```bash
git clone https://github.com/Ryansousa10/unifor-challenge-backend.git
cd unifor-challenge-backend
```

## 🐳 Execução 100% Docker

Todo o sistema pode ser executado apenas com Docker e Docker Compose, sem necessidade de instalar Node.js, NPM, Angular CLI, Java ou PostgreSQL localmente.

Basta rodar:

```bash
docker-compose up --build -d
```

- O frontend estará disponível em: http://localhost:4200
- Os logins para acessar o sistema são:


- ADMIN:
  - Usuário: `admin`
  - Senha: `admin`
- COORDENADOR:
  - Usuário: `coordenador`
  - Senha: `coordenador`
- PROFESSOR:
  - Usuário: `professor`
  - Senha: `professor`
- ALUNO:
  - Usuário: `aluno`
  - Senha: `aluno`
  
### Permissões por Papel

| Recurso      | ADMIN | COORDENADOR | PROFESSOR | ALUNO |
|--------------|:-----:|:-----------:|:---------:|:-----:|
| Usuários     | CRUD  |      R      |     R     |   R   |
| Cursos       | CRUD  |    CRUD     |     R     |   R   |
| Disciplinas  | CRUD  |    CRUD     |     R     |   R   |
| Currículos   | CRUD  |    CRUD     |     R     |   R   |
| Semestres    | CRUD  |    CRUD     |     R     |   R   |

CRUD = Criar, Listar, Atualizar, Remover; R = Listar/Visualizar

- O backend estará disponível em: http://localhost:8081
- O Keycloak estará disponível em: http://localhost:8080
- O banco de dados PostgreSQL estará disponível em: localhost:5432

O build do frontend Angular é feito automaticamente no container e servido via nginx. O backend Java é buildado e executado no container. O Keycloak e os bancos de dados também sobem automaticamente.

Se desejar rodar apenas um serviço (por exemplo, só o backend ou só o frontend), utilize:

```bash
docker-compose up backend
```

ou

```bash
docker-compose up frontend
```

Consulte o arquivo `docker-compose.yml` para mais detalhes e customizações.

---

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

[Documentação completa disponível no Swagger unifor-backend\src\main\resources\openapi.yaml]

---

## 🔐 Autenticação e Autorização

O sistema utiliza Keycloak com:

* Realm: `unifor`
* Client ID: `unifor-backend`
* Roles:
  - ADMIN
  - COORDENADOR
  - PROFESSOR
  - ALUNO

* **Keycloak**: realm `unifor`, client `unifor-backend` (tipo service), roles definidas (`ADMIN`, `COORDENADOR`, `PROFESSOR`, `ALUNO`).
* **Quarkus OIDC**: configurado em `application.properties` e sobrescrito no `docker-compose.yml`.
* Endpoints protegidos por `@RolesAllowed` e políticas RESTEasy Reactive.
---
## 🧪 Testes

O projeto possui testes unitários e de integração utilizando JUnit 5 e Quarkus Test Framework. Os testes estão organizados em três pacotes principais:

1. **Migrations**: Validação do banco de dados
2. **Services**: Lógica de negócio
3. **Controllers**: Endpoints da API

Para executar os testes:
```bash
./mvnw test
```

## 📊 Monitoramento

- Métricas: `/q/metrics`
- Health checks: `/q/health`
- Logs: Configurados em níveis (INFO, DEBUG) por categoria

# Desafio Desenvolvedor Unifor

Este projeto implementa um backend em Quarkus para gerenciamento de usuários, perfis (roles) e entidades acadêmicas (semestres, currículos, disciplinas, matrículas), integrado com Keycloak para autenticação e autorização, e PostgreSQL como banco de dados, com migrações gerenciadas pelo Flyway.

---

## 📦 Arquitetura

O sistema é composto por quatro serviços Docker:

* **Keycloak-DB**: PostgreSQL para armazenar dados do Keycloak.
* **Keycloak**: Servidor de autenticação e autorização (realm `unifor`).
* **Unifor-DB**: PostgreSQL para dados da aplicação.
* **Backend**: Aplicação Quarkus escrita em Java 17.

O orquestrador é definido em `docker-compose.yml`.

---

## ⚙️ Configuração

As configurações da aplicação estão em `application.properties`, mas podem ser sobrescritas via variáveis de ambiente no Docker Compose (`quarkus.oidc.*`, `quarkus.datasource.*`, etc.).

Certifique-se de ajustar, se necessário:

* Credenciais do PostgreSQL (`unifor-db` e `keycloak-db`).
* Secret do cliente Keycloak (`unifor-backend`).

## 📑 Endpoints REST

### Users (`/users`)

* `GET    /users` — lista todos os usuários.

* `GET    /users/{id}` — obtém usuário por ID.

* `POST   /users` — cria usuário; JSON exemplo:
  ```json
  {
    "username": "adminuser",
    "password": "senha123",
    "firstName": "Administrador",
    "lastName": "Sistema",
    "email": "admin@exemplo.com",
    "roles": ["UUID-DA-ROLE-ADMIN"]
  }
  ```
* `PUT    /users/{id}` — atualiza dados do usuário.

* `DELETE /users/{id}` — remove usuário.

### Cursos (`/course`)
* `GET    /course` — lista todos os cursos.
* `GET    /course/{id}` — obtém curso por ID.
* `POST   /course` — cria curso; JSON exemplo:
  ```json
  {
  "code": "ENG001",
  "name": "Engenharia Civil",
  "description": "Descrição do curso."
  }
  ```
* `PUT    /course/{id}` — atualiza curso.
* `DELETE /course/{id}` — remove curso.

### Disciplina (`/discipline`)

* `GET    /discipline`
* `GET    /discipline/{id}`
* `POST   /discipline`
 ```json
  {
  "code": "MAT001",
  "name": "Cálculo Diferencial e Integral",
  "credits": 4,
  "workload": 80,
  "description": "Estudo de limites, derivadas e integrais de funções de uma variável real."
}
 ```
* `PUT    /discipline/{id}`
* `DELETE /discipline/{id}`

### Semestres (`/semester`)

* `GET    /semester`
* `GET    /semester/{id}`
* `POST   /semester`
 ```json
  {
  "name": "2025.1",
  "startDate": "2025-01-15",
  "endDate": "2025-06-30"
}
  ```
* `PUT    /semester/{id}`
* `DELETE /semester/{id}`

### Currículo (`/curriculum`)

* `GET    /curriculum`
* `GET    /curriculum/{id}`
* `POST   /curriculum`
```json
{
  "name": "Matriz Engenharia 2025",
  "description": "Matriz curricular para o curso de Engenharia 2025.",
  "active": true,
  "courseId": "UUID-DO-CURSO-1",
  "semesterId": "UUID-DO-SEMESTRE-1",
  "disciplines": [
    {
      "disciplineId": "UUID-DA-DISCIPLINA-1",
      "ordering": 1
    },
    {
      "disciplineId": "UUID-DA-DISCIPLINA-2",
      "ordering": 2
    }
  ]
}
```
* `PUT    /curriculum/{id}`
* `DELETE /curriculum/{id}`

## 🔄 Migrations (Flyway)

As migrations ficam em `db/migration`:

* **V1**: cria tabelas `role`, `users`, `user_role`.
* **V2**: cria tabelas `course`, `semester`, `discipline`.
* **V3**: cria tabela `curriculum` e relaciona com disciplinas.
* **V4**: adiciona tabela `curric_disc` para relacionar disciplinas com currículos.

O Flyway executa automaticamente no startup (`baseline-on-migrate=true`).

---

## 📚 Documentação OpenAPI

A documentação completa da API está disponível no arquivo `src/main/resources/openapi.yaml` e pode ser visualizada em ferramentas como Swagger Editor ou Insomnia.

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).