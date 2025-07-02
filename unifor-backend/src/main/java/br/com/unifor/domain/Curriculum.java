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
@Table(
        name = "curriculum",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "semester_id"})
)
public class Curriculum extends PanacheEntityBase {
    // Representa a matriz curricular de um curso em um semestre específico.
    // Associa um Course a um Semester, permitindo a montagem da grade de disciplinas por período.
    // A restrição de unicidade garante que não existam matrizes duplicadas para o mesmo curso e semestre.
    // Utiliza UUID como identificador e relacionamentos ManyToOne para curso e semestre.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;
}