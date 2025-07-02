package br.com.unifor.rest;

import br.com.unifor.domain.User;
import br.com.unifor.domain.Role;
import br.com.unifor.dto.UserRequestDTO;
import br.com.unifor.dto.UserResponseDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class UserResource {

    // Recurso REST responsável pelo gerenciamento de usuários do sistema.
    // Disponível apenas para usuários com perfil ADMIN, conforme @RolesAllowed.
    // Implementa operações CRUD básicas para a entidade User.
    //
    // Decisão: O controle de acesso é feito via Keycloak/Quarkus Security, garantindo que apenas administradores possam manipular usuários.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @GET
    @RolesAllowed("ADMIN")
    public List<UserResponseDTO> list() {
        return User.<User>listAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response get(@PathParam("id") UUID id) {
        User u = User.findById(id);
        return u != null
                ? Response.ok(toResponseDTO(u)).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    @Transactional
    public Response create(UserRequestDTO dto, @Context UriInfo uriInfo) {
        User user = toEntity(dto);
        user.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(user.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response update(@PathParam("id") UUID id, UserRequestDTO dto) {
        User existing = User.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setRoles(fetchRolesByIds(dto.getRoles()));
        return Response.ok(toResponseDTO(existing)).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = User.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

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
                        : Collections.emptySet()
        );
        return dto;
    }

    private User toEntity(UserRequestDTO dto) {
        Set<Role> roles = fetchRolesByIds(dto.getRoles());
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .roles(roles)
                .build();
    }

    private Set<Role> fetchRolesByIds(Set<UUID> roleIds) {
        if (roleIds == null) return Collections.emptySet();
        return roleIds.stream()
                .map(id -> (Role) Role.findById(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}