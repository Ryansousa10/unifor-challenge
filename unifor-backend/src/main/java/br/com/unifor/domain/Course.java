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
@Table(name = "course")
public class Course extends PanacheEntityBase {

    // Entidade que representa um curso da instituição.
    //
    // Atributos:
    // - id: identificador único do curso (UUID)
    // - code: código institucional do curso (único)
    // - name: nome completo do curso
    // - description: descrição detalhada do curso (opcional)
    //
    // Relacionamentos:
    // - Um curso pode ter várias matrizes curriculares (Curriculum)
    //
    // Regras:
    // - O código e nome são obrigatórios
    // - O código deve ser único no sistema
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de unicidade são realizadas antes da persistência

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
