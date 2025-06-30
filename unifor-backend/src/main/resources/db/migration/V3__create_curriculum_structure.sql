CREATE TABLE curriculum (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            course_id   UUID NOT NULL REFERENCES course(id) ON DELETE CASCADE,
                            semester_id UUID NOT NULL REFERENCES semester(id) ON DELETE CASCADE
);

CREATE TABLE curric_disc (
                             curriculum_id UUID NOT NULL REFERENCES curriculum(id) ON DELETE CASCADE,
                             discipline_id UUID NOT NULL REFERENCES discipline(id) ON DELETE CASCADE,
                             ordering      INTEGER NOT NULL,              -- posição na matriz
                             PRIMARY KEY (curriculum_id, discipline_id)
);
