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
    // Representa uma disciplina (matéria) do curso.
    // Utiliza UUID como identificador e código único para facilitar integrações e buscas.
    // O campo 'credits' representa a carga horária ou créditos da disciplina.
    // O campo 'description' permite detalhar o conteúdo da disciplina.
    //
    // Decisão: O campo 'code' é limitado a 20 caracteres e único.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer credits;

    @Column(columnDefinition = "TEXT")
    private String description;
}
