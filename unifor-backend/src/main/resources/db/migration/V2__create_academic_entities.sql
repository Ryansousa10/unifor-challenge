CREATE TABLE course (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  description TEXT
);

CREATE TABLE semester (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(20) NOT NULL UNIQUE,    -- ex: "2025.1"
  start_date DATE NOT NULL,
  end_date   DATE NOT NULL
);

CREATE TABLE discipline (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  credits INTEGER NOT NULL,
  description TEXT
);
