package br.com.unifor.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
public class Role extends PanacheEntityBase {

    // Representa o perfil de acesso do usuário no sistema (ex: ADMIN, COORDENADOR, PROFESSOR, ALUNO).
    // Permite associação ManyToMany com usuários, possibilitando múltiplos perfis por usuário.
    //
    // Decisão: O campo 'name' é limitado a 20 caracteres e único para evitar duplicidade de perfis.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String name; // ADMIN, COORDENADOR, PROFESSOR, ALUNO

}