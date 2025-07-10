-- Adiciona os campos name, description e active à tabela curriculum
ALTER TABLE curriculum
    ADD COLUMN name VARCHAR(255) NOT NULL DEFAULT 'Sem nome',
    ADD COLUMN description TEXT NOT NULL DEFAULT '',
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

