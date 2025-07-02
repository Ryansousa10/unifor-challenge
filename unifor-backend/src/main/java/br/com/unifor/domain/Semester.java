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
    // Representa um semestre letivo (ex: 2024.1).
    // Utiliza UUID como identificador e termo único para evitar duplicidade.
    // O termo segue padrão como "2024.1" ou "2024.2".
    // As datas de início e fim são obrigatórias para controle de períodos acadêmicos.
    //
    // Decisão: O campo 'term' é limitado a 20 caracteres e único.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String term;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
