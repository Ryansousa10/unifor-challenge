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


## 🏗 Arquitetura

O sistema é composto por quatro serviços Docker:

* **Keycloak-DB**: PostgreSQL para armazenar dados do Keycloak
* **Keycloak**: Servidor de autenticação e autorização (realm `unifor`)
* **Unifor-DB**: PostgreSQL para dados da aplicação
* **Backend**: Aplicação Quarkus escrita em Java 17
* **Frontend**: Aplicação Angular (não incluída neste repositório, mas mencionada para contexto)

  <img width="2590" height="1045" alt="2" src="https://github.com/user-attachments/assets/9415d4a4-458c-4604-8a4d-aca0eec976c2" />

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

---

## 📄 Licença

Este projeto está sob a licença MIT.
