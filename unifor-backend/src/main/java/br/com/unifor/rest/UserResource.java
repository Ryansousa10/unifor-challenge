package br.com.unifor.rest;

import br.com.unifor.dto.UserRequestDTO;
import br.com.unifor.dto.UserResponseDTO;
import br.com.unifor.service.UserService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @Inject
    UserService userService;

    @GET
    @RolesAllowed("ADMIN")
    public Response list() {
        List<UserResponseDTO> users = userService.listUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response get(@PathParam("id") UUID id) {
        UserResponseDTO dto = userService.getUser(id);
        return Response.ok(dto).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid UserRequestDTO dto, @Context UriInfo uriInfo) {
        UserResponseDTO created = userService.createUser(dto);
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(created.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") UUID id, @Valid UserRequestDTO dto) {
        UserResponseDTO updated = userService.updateUser(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") UUID id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }
}