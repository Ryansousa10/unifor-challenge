CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- roles e usuários
CREATE TABLE role (
                      id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                      name VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('COORDENADOR'),
                            ('PROFESSOR'),
                            ('ALUNO');

CREATE TABLE "user" (
                        id         UUID      PRIMARY KEY DEFAULT uuid_generate_v4(),
                        username   VARCHAR(50) NOT NULL UNIQUE,
                        password   VARCHAR(255) NOT NULL,
                        first_name VARCHAR(50),
                        last_name  VARCHAR(50),
                        email      VARCHAR(100) UNIQUE,
                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

COMMENT ON COLUMN "user".created_at IS 'Data e hora de criação do registro';

CREATE TABLE user_role (
                           user_id UUID NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
                           role_id UUID NOT NULL REFERENCES role(id) ON DELETE CASCADE,
                           PRIMARY KEY (user_id, role_id)
);
