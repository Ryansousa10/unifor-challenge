package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role extends PanacheEntityBase {
    @Id
    public UUID id;

    @Column(nullable = false, unique = true, length = 20)
    public String name;  // ADMIN, COORDENADOR, PROFESSOR, ALUNO

    @PrePersist
    void prePersist() {
        this.id = UUID.randomUUID();
    }
}