CREATE TABLE course (
  id UUID NOT NULL DEFAULT RANDOM_UUID(),
  code VARCHAR(20) NOT NULL,
  name VARCHAR(100) NOT NULL,
  description CLOB,
  CONSTRAINT pk_course PRIMARY KEY (id),
  CONSTRAINT uq_course_code UNIQUE (code)
);

CREATE TABLE semester (
  id UUID NOT NULL DEFAULT RANDOM_UUID(),
  name VARCHAR(20) NOT NULL,    -- ex: "2025.1"
  start_date DATE NOT NULL,
  end_date   DATE NOT NULL,
  CONSTRAINT pk_semester PRIMARY KEY (id),
  CONSTRAINT uq_semester_name UNIQUE (name)
);

CREATE TABLE discipline (
  id UUID NOT NULL DEFAULT RANDOM_UUID(),
  code VARCHAR(20) NOT NULL,
  name VARCHAR(100) NOT NULL,
  credits INTEGER NOT NULL,
  description CLOB,
  CONSTRAINT pk_discipline PRIMARY KEY (id),
  CONSTRAINT uq_discipline_code UNIQUE (code)
);
