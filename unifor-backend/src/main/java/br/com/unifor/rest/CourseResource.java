package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.domain.Course;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"COORDENADOR", "ADMIN"})
public class CourseResource {

    // Recurso REST responsável pelo gerenciamento de cursos da instituição.
    // Disponível apenas para usuários com perfil COORDENADOR ou ADMIN, conforme @RolesAllowed.
    // Implementa operações CRUD básicas para a entidade Course.
    //
    // Decisão: O campo 'code' é considerado único e representa o código institucional do curso.
    // O campo 'description' é opcional e pode detalhar o curso.
    // O método persist() do Panache cuida da geração do UUID do curso.
    // O método deleteById retorna true se o curso foi removido, false se não encontrado.
    // O método update atualiza todos os campos editáveis do curso, exceto o id.
    // O endpoint está protegido por autenticação e autorização via Keycloak.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @GET
    public List<Course> list() {
        return Course.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        Course c = Course.findById(id);
        return c != null
                ? Response.ok(c).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(Course course, @Context UriInfo uriInfo) {
        course.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(course.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, Course updated) {
        Course existing = Course.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return Response.ok(existing).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Course.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}