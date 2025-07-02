package br.com.unifor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {
    // Representa um usuário do sistema, podendo ser administrador, coordenador, professor ou aluno.
    // Utiliza UUID como identificador e possui relacionamento com perfis (roles) para controle de acesso.
    // As anotações Lombok (@Data, @NoArgsConstructor, etc.) reduzem o boilerplate de getters/setters e construtores.
    // As anotações JPA mapeiam a entidade para o banco de dados.
    //
    // Decisão: O campo 'password' está presente para fins de exemplo, mas em produção recomenda-se delegar autenticação ao Keycloak.
    // O relacionamento ManyToMany com Role permite múltiplos perfis por usuário.
    // O campo 'createdAt' é preenchido automaticamente na criação do registro.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
