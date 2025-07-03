CREATE TABLE enrollment (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    student_id UUID NOT NULL,
    curriculum_id UUID NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT pk_enrollment PRIMARY KEY (id),
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_curriculum FOREIGN KEY (curriculum_id) REFERENCES curriculum(id) ON DELETE CASCADE
);

