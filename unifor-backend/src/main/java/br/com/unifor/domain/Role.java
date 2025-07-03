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

    // Entidade que representa um perfil de acesso ao sistema.
    //
    // Atributos:
    // - id: identificador único do perfil (UUID)
    // - name: nome do perfil (ex: ADMIN, COORDENADOR)
    //
    // Relacionamentos:
    // - Um perfil pode estar associado a vários usuários (User)
    //
    // Regras:
    // - Nome do perfil deve ser único
    // - Nome do perfil é convertido para maiúsculas
    // - Perfis padrão: ADMIN, COORDENADOR, PROFESSOR, ALUNO
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Integração com permissões do Keycloak

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String name; // ADMIN, COORDENADOR, PROFESSOR, ALUNO

}