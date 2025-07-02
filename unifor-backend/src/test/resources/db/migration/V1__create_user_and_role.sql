-- roles e usuários
CREATE TABLE role (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    name VARCHAR(20) NOT NULL UNIQUE,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

INSERT INTO role (name) VALUES
    ('ADMIN'),
    ('COORDENADOR'),
    ('PROFESSOR'),
    ('ALUNO');

CREATE TABLE users (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

COMMENT ON COLUMN users.created_at IS 'Data e hora de criação do registro';

CREATE TABLE user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);
