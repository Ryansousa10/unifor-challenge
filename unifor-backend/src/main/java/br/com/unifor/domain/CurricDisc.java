package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "curric_disc")
@IdClass(CurricDiscId.class)
public class CurricDisc extends PanacheEntityBase {

    // Modela a relação N:N entre Curriculum (matriz curricular) e Discipline (disciplina).
    // Utiliza chave composta (curriculumId, disciplineId) para garantir unicidade da associação.
    // O campo 'ordering' define a ordem da disciplina na matriz curricular.
    //
    // Decisão: Utiliza @IdClass para chave composta e facilita consultas e manutenção.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @Column(name = "curriculum_id")
    private UUID curriculumId;

    @Id
    @Column(name = "discipline_id")
    private UUID disciplineId;

    @Column(name = "ordering", nullable = false)
    private Integer ordering;
}