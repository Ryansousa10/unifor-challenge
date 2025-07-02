package br.com.unifor.rest;

import br.com.unifor.domain.Role;
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

    // Recurso REST responsável pelo gerenciamento de perfis (roles) de acesso do sistema.
    // Disponível apenas para usuários com perfil ADMIN, conforme @RolesAllowed.
    // Implementa operações CRUD básicas para a entidade Role.
    //
    // Decisão: O controle de acesso é feito via Keycloak/Quarkus Security, garantindo que apenas administradores possam manipular perfis.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @GET
    public List<Role> list() {
        return Role.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        Role role = Role.findById(id);
        return role != null
                ? Response.ok(role).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}