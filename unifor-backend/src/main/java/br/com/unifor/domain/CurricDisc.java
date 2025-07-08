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

    // Entidade que representa o vínculo entre uma matriz curricular e uma disciplina.
    //
    // Atributos:
    // - curriculumId: identificador da matriz curricular
    // - disciplineId: identificador da disciplina
    // - ordering: ordem/período sugerido da disciplina na matriz
    //
    // Relacionamentos:
    // - Associa uma Matriz Curricular (Curriculum) a uma Disciplina (Discipline)
    //
    // Regras:
    // - A combinação curriculumId + disciplineId deve ser única
    // - O ordering define a sequência sugerida das disciplinas
    //
    // Observações:
    // - Utiliza chave composta (IdClass)
    // - Permite ordenação das disciplinas na matriz

    @Id
    @Column(name = "curriculum_id")
    private UUID curriculumId;

    @Id
    @Column(name = "discipline_id")
    private UUID disciplineId;

    @Column(name = "ordering", nullable = false)
    private Integer ordering;
}