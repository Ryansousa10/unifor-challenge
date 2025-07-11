package br.com.unifor.service;

import br.com.unifor.domain.Role;
import br.com.unifor.domain.User;
import br.com.unifor.dto.UserRequestDTO;
import br.com.unifor.dto.UserResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    // Serviço responsável pela lógica de negócio relacionada a usuários.
    // Implementa as operações CRUD e regras específicas para a entidade User.
    //
    // Regras de negócio:
    // - Email deve ser único no sistema
    // - Usuário deve ter pelo menos um perfil (Role)
    // - Senha é gerenciada pelo Keycloak
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Integração com Keycloak para autenticação
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<UserResponseDTO> listUsers() {
        return User.listAll().stream()
                .map(u -> toResponseDTO((User) u))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUser(UUID id) {
        User user = (User) User.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        // Validação de duplicidade de email e username
        if (User.find("email", dto.getEmail()).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Email já cadastrado: " + dto.getEmail());
        }
        if (User.find("username", dto.getUsername()).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Username já cadastrado: " + dto.getUsername());
        }
        // Validação de payload: pelo menos um perfil
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Usuário deve ter pelo menos um perfil (role)");
        }
        User user = toEntity(dto);
        user.persist();
        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        User existing = (User) User.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        // Validação de duplicidade de email e username (exceto o próprio usuário)
        if (User.find("email = ?1 and id <> ?2", dto.getEmail(), id).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Email já cadastrado: " + dto.getEmail());
        }
        if (User.find("username = ?1 and id <> ?2", dto.getUsername(), id).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Username já cadastrado: " + dto.getUsername());
        }
        // Validação de payload: pelo menos um perfil
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Usuário deve ter pelo menos um perfil (role)");
        }
        // atualiza campos
        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setRoles(fetchRolesByIds(dto.getRoles()));
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteUser(UUID id) {
        boolean deleted = User.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("User not found: " + id);
        }
    }

    // mapeia entidade -> DTO (sem mudar o UserResponseDTO)
    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRoles(
                user.getRoles() != null
                        ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                        : Set.of()
        );
        return dto;
    }

    // mapeia DTO -> entidade (sem tocar o UserRequestDTO)
    private User toEntity(UserRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .roles(fetchRolesByIds(dto.getRoles()))
                .build();
    }

    // busca as roles que vieram como IDs no DTO
    private Set<Role> fetchRolesByIds(Set<UUID> roleIds) {
        if (roleIds == null) {
            return Set.of();
        }
        return roleIds.stream()
                .map(id -> (Role) Role.findByIdOptional(id)
                        .orElseThrow(() -> new NotFoundException("Role not found: " + id)))
                .collect(Collectors.toSet());
    }
}