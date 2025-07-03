# Sistema de Gestão Acadêmica - UNIFOR Backend

Este é o backend do sistema de gestão acadêmica da UNIFOR, desenvolvido com Quarkus, PostgreSQL e autenticação via Keycloak.

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

### Para Usuários (Testando a Aplicação)

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

## 🔐 Autenticação

O sistema utiliza Keycloak para autenticação e autorização, com os seguintes papéis:

- ADMIN
- COORDENADOR
- PROFESSOR
- ALUNO

## 🌐 API Endpoints

### Usuários
- GET /api/users - Lista todos os usuários
- POST /api/users - Cria novo usuário
- GET /api/users/{id} - Obtém usuário específico
- PUT /api/users/{id} - Atualiza usuário
- DELETE /api/users/{id} - Remove usuário

### Cursos
- GET /api/courses - Lista todos os cursos
- POST /api/courses - Cria novo curso
- GET /api/courses/{id} - Obtém curso específico
- PUT /api/courses/{id} - Atualiza curso
- DELETE /api/courses/{id} - Remove curso

### Disciplinas
- GET /api/disciplines - Lista todas as disciplinas
- POST /api/disciplines - Cria nova disciplina
- GET /api/disciplines/{id} - Obtém disciplina específica
- PUT /api/disciplines/{id} - Atualiza disciplina
- DELETE /api/disciplines/{id} - Remove disciplina

### Semestres
- GET /api/semesters - Lista todos os semestres
- POST /api/semesters - Cria novo semestre
- GET /api/semesters/{id} - Obtém semestre específico
- PUT /api/semesters/{id} - Atualiza semestre
- DELETE /api/semesters/{id} - Remove semestre

### Currículos
- GET /api/curricula - Lista todos os currículos
- POST /api/curricula - Cria novo currículo
- GET /api/curricula/{id} - Obtém currículo específico
- PUT /api/curricula/{id} - Atualiza currículo
- DELETE /api/curricula/{id} - Remove currículo

### Matrículas
- GET /api/enrollments - Lista todas as matrículas
- POST /api/enrollments - Cria nova matrícula
- GET /api/enrollments/{id} - Obtém matrícula específica
- PUT /api/enrollments/{id} - Atualiza matrícula
- DELETE /api/enrollments/{id} - Remove matrícula

## 🧪 Testes

O projeto possui testes automatizados cobrindo:

1. **Migrations**: Validação da estrutura do banco de dados
   - Colunas e tipos
   - Constraints (PK, FK, UNIQUE)
   - Relacionamentos

2. **Services**: Lógica de negócio
   - Validações
   - Regras de negócio
   - Manipulação de dados

3. **Controllers**: Endpoints da API
   - Validação de requisições
   - Códigos de status HTTP
   - Respostas formatadas

Para executar os testes:
```bash
./mvnw test
```

## 📦 Build e Deploy

Para gerar o JAR da aplicação:
```bash
./mvnw package
```

Para gerar a imagem Docker:
```bash
docker build -f src/main/docker/Dockerfile.jvm -t unifor-backend:latest .
```

## 🔍 Monitoramento

A aplicação expõe métricas através do endpoint: `/q/metrics`

Health checks disponíveis em: `/q/health`

## 📄 Licença

Este projeto está sob a licença MIT.
