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
    // Representa um curso da instituição (ex: Engenharia, Administração).
    // Utiliza UUID como identificador e possui código único para facilitar integrações e buscas.
    // As anotações Lombok (@Data, @NoArgsConstructor, etc.) reduzem o boilerplate de getters/setters e construtores.
    // As anotações JPA mapeiam a entidade para o banco de dados.
    // O campo 'description' permite detalhar o curso e utiliza o tipo TEXT para maior flexibilidade.
    //
    // Decisão: O campo 'code' é limitado a 20 caracteres e único para evitar duplicidade de cursos.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

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
