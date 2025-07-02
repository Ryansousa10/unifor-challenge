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

    // Representa a matrícula de um aluno em uma matriz curricular específica.
    // Utiliza UUID como identificador e armazena o vínculo entre estudante e matriz curricular.
    // O campo 'enrolledAt' registra a data/hora da matrícula.
    //
    // Decisão: Os campos studentId e curriculumId são UUIDs para facilitar integrações e garantir unicidade.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

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