CREATE TABLE enrollment (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            student_id    UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            curriculum_id UUID NOT NULL REFERENCES curriculum(id) ON DELETE CASCADE,
                            enrolled_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);
