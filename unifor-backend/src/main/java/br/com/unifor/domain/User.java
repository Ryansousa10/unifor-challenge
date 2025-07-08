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

    // Entidade que representa um usuário do sistema.
    //
    // Atributos:
    // - id: identificador único do usuário (UUID)
    // - username: nome de usuário para login
    // - password: senha criptografada
    // - firstName: nome do usuário
    // - lastName: sobrenome do usuário
    // - email: email do usuário (único)
    // - createdAt: data de criação do registro
    // - roles: perfis de acesso do usuário
    //
    // Relacionamentos:
    // - Um usuário pode ter múltiplos perfis (Role)
    //
    // Regras:
    // - Email e username devem ser únicos
    // - Senha é gerenciada pelo Keycloak
    // - Cada usuário deve ter ao menos um perfil
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Integração com Keycloak para autenticação

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
