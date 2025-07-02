package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
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