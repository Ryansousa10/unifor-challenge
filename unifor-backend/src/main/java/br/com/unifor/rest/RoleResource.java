package br.com.unifor.rest;

import br.com.unifor.dto.RoleRequestDTO;
import br.com.unifor.dto.RoleResponseDTO;
import br.com.unifor.service.RoleService;
import jakarta.inject.Inject;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class RoleResource {

    @Inject
    RoleService roleService;

    // Recurso REST para gerenciamento de perfis de acesso.
    // Controla os tipos de perfis disponíveis no sistema.
    //
    // Decisões técnicas:
    // - Campo 'name' é único e representa o nome do perfil
    // - Perfis padrão: ALUNO, PROFESSOR, COORDENADOR, ADMIN
    //
    // Permissões:
    // - Todas as operações restritas a ADMIN
    //
    // Observações:
    // - IDs são gerados automaticamente como UUID
    // - Integração com permissões do Keycloak

    @GET
    public List<RoleResponseDTO> list() {
        return roleService.listRoles();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        RoleResponseDTO dto = roleService.getRole(id);
        return Response.ok(dto).build();
    }

    @POST
    public Response create(RoleRequestDTO dto) {
        RoleResponseDTO created = roleService.createRole(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, RoleRequestDTO dto) {
        RoleResponseDTO updated = roleService.updateRole(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        roleService.deleteRole(id);
        return Response.noContent().build();
    }
}