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

    @Id
    @Column(name = "curriculum_id")
    private UUID curriculumId;

    @Id
    @Column(name = "discipline_id")
    private UUID disciplineId;

    @Column(name = "ordering", nullable = false)
    private Integer ordering;
}