package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "semester")
public class Semester extends PanacheEntityBase {

    // Entidade que representa um semestre letivo.
    //
    // Atributos:
    // - id: identificador único do semestre (UUID)
    // - name: identificador do semestre (ex: 2023.1)
    // - startDate: data de início do semestre
    // - endDate: data de término do semestre
    //
    // Relacionamentos:
    // - Um semestre pode ter várias matrizes curriculares (Curriculum)
    //
    // Regras:
    // - Nome e datas são obrigatórios
    // - O nome deve ser único no sistema
    // - Data de início deve ser anterior à data de fim
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de datas são realizadas antes da persistência

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
