package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.dto.CourseRequestDTO;
import br.com.unifor.dto.CourseResponseDTO;
import br.com.unifor.service.CourseService;
import jakarta.inject.Inject;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"COORDENADOR", "ADMIN"})
public class CourseResource {

    // Recurso REST para gerenciamento de cursos da instituição.
    // Controla o cadastro e manutenção dos cursos disponíveis.
    //
    // Decisões técnicas:
    // - Campo 'code' é único e representa o código institucional do curso
    // - Campo 'description' é opcional para detalhamento
    //
    // Permissões:
    // - Todas as operações restritas a COORDENADOR e ADMIN
    //
    // Observações:
    // - IDs são gerados automaticamente como UUID
    // - Autenticação e autorização via Keycloak

    @Inject
    CourseService courseService;

    @GET
    public List<CourseResponseDTO> list() {
        return courseService.listCourses();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        CourseResponseDTO dto = courseService.getCourse(id);
        return Response.ok(dto).build();
    }

    @POST
    public Response create(CourseRequestDTO dto, @Context UriInfo uriInfo) {
        CourseResponseDTO created = courseService.createCourse(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, CourseRequestDTO dto) {
        CourseResponseDTO updated = courseService.updateCourse(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        courseService.deleteCourse(id);
        return Response.noContent().build();
    }
}