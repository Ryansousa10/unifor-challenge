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