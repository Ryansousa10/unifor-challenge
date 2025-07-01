package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "enrollment")
public class Enrollment extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "curriculum_id", nullable = false)
    private UUID curriculumId;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;
}