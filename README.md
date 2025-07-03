# Sistema de Gestão Acadêmica - UNIFOR Backend

Este é o backend do sistema de gestão acadêmica do desafio UNIFOR, uma aplicação robusta desenvolvida com Quarkus, PostgreSQL e autenticação via Keycloak, oferecendo uma gestão completa de usuários, perfis e entidades acadêmicas.

## 📋 Índice

- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração e Instalação](#-configuração-e-instalação)
- [Estrutura do Banco de Dados](#-estrutura-do-banco-de-dados)
- [API Endpoints](#-api-endpoints)
- [Autenticação e Autorização](#-autenticação-e-autorização)
- [Testes](#-testes)
- [Build e Deploy](#-build-e-deploy)
- [Monitoramento](#-monitoramento)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## 🏗 Arquitetura

O sistema é composto por quatro serviços Docker:

* **Keycloak-DB**: PostgreSQL para armazenar dados do Keycloak
* **Keycloak**: Servidor de autenticação e autorização (realm `unifor`)
* **Unifor-DB**: PostgreSQL para dados da aplicação
* **Backend**: Aplicação Quarkus escrita em Java 17

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
  - Matrículas
  - Currículos

## 🛠 Tecnologias

* **Backend**
  - Java 17
  - Quarkus 3.24.1
  - Hibernate ORM com Panache
  - Camel Quarkus
  - RESTEasy Reactive
  - JUnit 5 (Testes)

* **Banco de Dados**
  - PostgreSQL
  - Flyway para migrações
  - UUID como identificadores

* **Segurança**
  - Keycloak
  - OpenID Connect (OIDC)
  - Autorização baseada em roles

## 🚀 Pré-requisitos

* Java 17 ou superior
* Maven 3.6+
* Docker 20.10+
* Docker Compose 2.0+
* PostgreSQL (opcional se usar Docker)

## ⚙️ Configuração e Instalação

1. Clone o repositório:
```bash
git clone https://github.com/Ryansousa10/unifor-challenge-backend.git
cd unifor-challenge-backend
```

2. Inicie os serviços com Docker Compose:
```bash
docker-compose up -d
```

O sistema estará disponível em:
- Backend: http://localhost:8080
- Keycloak: http://localhost:8180

## 📂 Estrutura do Banco de Dados

O sistema utiliza migrations (Flyway) para gerenciar a estrutura:

* **V1: Usuários e Papéis**
  - users (usuários)
  - role (papéis)
  - user_role (relacionamento)

* **V2: Entidades Acadêmicas**
  - course (cursos)
  - semester (semestres)
  - discipline (disciplinas)

* **V3: Estrutura Curricular**
  - curriculum
  - curric_disc

* **V4: Matrículas**
  - enrollment

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

### Semestres, Currículos e Matrículas
[Documentação completa disponível no Swagger unifor-backend\src\main\resources\openapi.yaml]

## 🔐 Autenticação e Autorização

O sistema utiliza Keycloak com:

* Realm: `unifor`
* Client ID: `unifor-backend`
* Roles:
  - ADMIN
  - COORDENADOR
  - PROFESSOR
  - ALUNO

## 🧪 Testes

O projeto possui testes automatizados cobrindo:

1. **Migrations**: Validação do banco de dados
2. **Services**: Lógica de negócio
3. **Controllers**: Endpoints da API

Para executar os testes:
```bash
./mvnw test
```

## 📦 Build e Deploy

Gerando o JAR:
```bash
./mvnw package
```

Gerando imagem Docker:
```bash
docker build -f src/main/docker/Dockerfile.jvm -t unifor-backend:latest .
```

## 📊 Monitoramento

- Métricas: `/q/metrics`
- Health checks: `/q/health`
- Logs: Configurados em níveis (INFO, DEBUG) por categoria

## 📄 Licença

Este projeto está sob a licença MIT.
=======
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

## 🚀 Pré-requisitos

* [Docker](https://www.docker.com/get-started)
* [Docker Compose](https://docs.docker.com/compose/)
* Java 17 (apenas se quiser rodar local sem Docker)
* Maven 3.6+ (idem)

---

## 📂 Estrutura do Projeto

```
project-root/
├── docker-compose.yml
├── keycloak/
│   └── realm-export.json
├── unifor-backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       └── main/java/br/com/unifor/{domain,rest,config,...}
└── db/
    └── migration/
        ├── V1__create_user_and_role.sql
        ├── V2__create_academic_entities.sql
        ├── V3__create_curriculum_structure.sql
        └── V4__create_enrollment_optional.sql
```

---

## ⚙️ Configuração

As configurações da aplicação estão em `application.properties`, mas podem ser sobrescritas via variáveis de ambiente no Docker Compose (`quarkus.oidc.*`, `quarkus.datasource.*`, etc.).

Certifique-se de ajustar, se necessário:

* Credenciais do PostgreSQL (`unifor-db` e `keycloak-db`).
* Secret do cliente Keycloak (`unifor-backend`).

---

## 🏗️ Build & Run

### Com Docker Compose

No diretório raiz:

```bash
docker-compose up --build
```

* **Keycloak** disponível em `http://localhost:8080`
* **Admin Console** em `http://localhost:9000`
* **Backend** disponível em `http://localhost:8081`

### Local (Quarkus Dev)

No diretório `unifor-backend`:

```bash
./mvnw clean compile quarkus:dev
```

O backend roda em `http://localhost:8080` (porta padrão Quarkus).

---

## 📑 Endpoints REST

Todos os endpoints retornam/consomem JSON e exigem `Authorization: Bearer <token>` com role `ADMIN` para operações de escrita.

### Roles (`/roles`)

* `GET    /roles` — lista todas as roles.
* `GET    /roles/{id}` — obtém role por ID.
* `POST   /roles` — cria uma nova role.
* `PUT    /roles/{id}` — atualiza nome da role.
* `DELETE /roles/{id}` — remove role.

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
    "roles": [ { "id": "UUID-DA-ROLE-ADMIN" } ]
  }
  ```

* `PUT    /users/{id}` — atualiza dados do usuário.

* `DELETE /users/{id}` — remove usuário.

### Semestres (`/semester`)

* `GET    /semester`
* `GET    /semester/{id}`
* `POST   /semester` — cria com JSON `{ "term":"2024.1", "startDate":"2024-02-01", "endDate":"2024-07-31" }`
* `PUT    /semester/{id}`
* `DELETE /semester/{id}`

### Currículo (`/curriculum`)

* `GET    /curriculum`
* `GET    /curriculum/{id}`
* `POST   /curriculum` — cria com JSON `{ "courseId":"...", "semesterId":"..." }`
* `PUT    /curriculum/{id}`
* `DELETE /curriculum/{id}`

### Disciplina (`/discipline`)

* `GET    /discipline`
* `GET    /discipline/{id}`
* `POST   /discipline` — cria com JSON `{ "code":"MAT101", "name":"Matemática I", "credits":4, "description":"Algebra básica" }`
* `PUT    /discipline/{id}`
* `DELETE /discipline/{id}`

### Matrícula (`/enrollment`)

* `GET    /enrollment`
* `GET    /enrollment/{id}`
* `POST   /enrollment` — cria com JSON \`{ "studentId":"...", "curriculumId":"..." }
* `DELETE /enrollment/{id}`

---

## 🔄 Migrations (Flyway)

As migrations ficam em `db/migration`:

* **V1**: cria tabelas `role`, `users`, `user_role`.
* **V2/V3**: recriam estrutura acadêmica.
* **V4**: adiciona tabela `enrollment`.

O Flyway executa automaticamente no startup (`baseline-on-migrate=true`).

---

## 🔒 Autenticação & Autorização

* **Keycloak**: realm `unifor`, client `unifor-backend` (tipo service), roles definidas (`ADMIN`, `COORDENADOR`, `PROFESSOR`, `ALUNO`).
* **Quarkus OIDC**: configurado em `application.properties` e sobrescrito no `docker-compose.yml`.
* Endpoints protegidos por `@RolesAllowed` e políticas RESTEasy Reactive.

---

## 🤝 Contribuindo

1. Fork este repositório.
2. Crie uma branch: `git checkout -b feature/nova-feature`.
3. Faça commit das suas mudanças: `git commit -m 'Adiciona nova feature'`.
4. Push para a branch: `git push origin feature/nova-feature`.
5. Abra um Pull Request.

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).