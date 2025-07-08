CREATE TABLE curriculum (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    course_id UUID NOT NULL,
    semester_id UUID NOT NULL,
    CONSTRAINT pk_curriculum PRIMARY KEY (id),
    CONSTRAINT fk_curriculum_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_curriculum_semester FOREIGN KEY (semester_id) REFERENCES semester(id) ON DELETE CASCADE,
    CONSTRAINT uq_curriculum_course_semester UNIQUE (course_id, semester_id)
);

CREATE TABLE curric_disc (
    curriculum_id UUID NOT NULL,
    discipline_id UUID NOT NULL,
    ordering INTEGER NOT NULL,              -- posição na matriz
    CONSTRAINT pk_curric_disc PRIMARY KEY (curriculum_id, discipline_id),
    CONSTRAINT fk_curric_disc_curriculum FOREIGN KEY (curriculum_id) REFERENCES curriculum(id) ON DELETE CASCADE,
    CONSTRAINT fk_curric_disc_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id) ON DELETE CASCADE
);
