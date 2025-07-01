package br.com.unifor.rest;

import br.com.unifor.domain.User;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class UserResource {

    @GET
    @RolesAllowed("ADMIN")
    public List<User> list() {
        return User.listAll();
    }

    @GET
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response get(@PathParam("id") UUID id) {
        User u = User.findById(id);
        return u != null
                ? Response.ok(u).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(User user, @Context UriInfo uriInfo) {
        user.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(user.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") UUID id, User updated) {
        User existing = User.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setRoles(updated.getRoles());
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = User.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}