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

    // Entidade que representa uma matriz curricular.
    //
    // Atributos:
    // - id: identificador único da matriz (UUID)
    // - course: curso ao qual a matriz pertence
    // - semester: semestre letivo da matriz
    //
    // Relacionamentos:
    // - Pertence a um Curso (Course)
    // - Pertence a um Semestre (Semester)
    // - Possui várias disciplinas através de CurricDisc
    //
    // Regras:
    // - Deve estar associada a um curso e um semestre
    // - Não pode haver mais de uma matriz para o mesmo curso/semestre
    // - Disciplinas são vinculadas através da entidade CurricDisc
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Possui constraint de unicidade para curso+semestre

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