package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "discipline")
public class Discipline extends PanacheEntityBase {

    // Entidade que representa uma disciplina acadêmica.
    //
    // Atributos:
    // - id: identificador único da disciplina (UUID)
    // - code: código institucional da disciplina (único)
    // - name: nome completo da disciplina
    // - credits: número de créditos
    // - workload: carga horária da disciplina em horas
    // - description: descrição detalhada da disciplina (opcional)
    //
    // Relacionamentos:
    // - Uma disciplina pode estar em várias matrizes curriculares (CurricDisc)
    //
    // Regras:
    // - O código, nome, créditos e carga horária são obrigatórios
    // - O código deve ser único no sistema
    // - Os créditos e a carga horária devem ser números positivos
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações são realizadas antes da persistência

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private Integer workload;

    @Column(columnDefinition = "TEXT")
    private String description;
}
