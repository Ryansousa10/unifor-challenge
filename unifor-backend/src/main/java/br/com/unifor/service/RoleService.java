package br.com.unifor.service;

import br.com.unifor.domain.Role;
import br.com.unifor.dto.RoleRequestDTO;
import br.com.unifor.dto.RoleResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleService {
    // Serviço responsável pela lógica de negócio relacionada a perfis de acesso.
    // Implementa as operações CRUD e regras específicas para a entidade Role.
    //
    // Regras de negócio:
    // - Nome do perfil deve ser único no sistema
    // - Perfis são utilizados para controle de acesso
    // - Perfis não podem ser excluídos se estiverem em uso
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Integração com Keycloak para controle de acesso
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<RoleResponseDTO> listRoles() {
        return Role.listAll().stream()
                .map(r -> toResponseDTO((Role) r))
                .collect(Collectors.toList());
    }

    public RoleResponseDTO getRole(UUID id) {
        Role role = Role.findById(id);
        if (role == null) {
            throw new NotFoundException("Role not found: " + id);
        }
        return toResponseDTO(role);
    }

    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.persist();
        return toResponseDTO(role);
    }

    @Transactional
    public RoleResponseDTO updateRole(UUID id, RoleRequestDTO dto) {
        Role existing = Role.findById(id);
        if (existing == null) {
            throw new NotFoundException("Role not found: " + id);
        }
        existing.setName(dto.getName());
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteRole(UUID id) {
        boolean deleted = Role.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Role not found: " + id);
        }
    }

    private RoleResponseDTO toResponseDTO(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}
