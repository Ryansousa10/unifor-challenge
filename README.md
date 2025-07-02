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
