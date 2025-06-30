package br.com.unifor.rest;

import br.com.unifor.domain.Role;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class RoleResource {

    @GET
    public List<Role> list() {
        return Role.listAll();
    }

    @GET
    @Path("{id}")
    public Role get(@PathParam("id") UUID id) {
        return Role.findById(id);
    }

    @POST
    public Response create(Role role, @Context UriInfo uriInfo) {
        role.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(role.id.toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    public Role update(@PathParam("id") UUID id, Role updated) {
        Role existing = Role.findById(id);
        existing.name = updated.name;
        return existing;
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Role.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}