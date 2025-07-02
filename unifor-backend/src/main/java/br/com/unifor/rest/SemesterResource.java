package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.dto.SemesterRequestDTO;
import br.com.unifor.dto.SemesterResponseDTO;
import br.com.unifor.service.SemesterService;
import jakarta.inject.Inject;

@Path("/semester")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"COORDENADOR", "ADMIN"})
public class SemesterResource {

    // Recurso REST responsável pelo gerenciamento de semestres acadêmicos da instituição.
    // Disponível apenas para usuários com perfil COORDENADOR ou ADMIN, conforme @RolesAllowed.
    // Implementa operações CRUD básicas para a entidade Semester.
    //
    // Decisão: O campo 'name' é considerado único e representa o identificador do período (ex: 2025.1).
    // Os campos 'startDate' e 'endDate' são obrigatórios e representam o período de vigência do semestre.
    // O método persist() do Panache cuida da geração do UUID do semestre.
    // O método deleteById retorna true se o semestre foi removido, false se não encontrado.
    // O método update atualiza todos os campos editáveis do semestre, exceto o id.
    // O endpoint está protegido por autenticação e autorização via Keycloak.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Inject
    SemesterService semesterService;

    @GET
    public List<SemesterResponseDTO> list() {
        return semesterService.listSemesters();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        SemesterResponseDTO dto = semesterService.getSemester(id);
        return Response.ok(dto).build();
    }

    @POST
    public Response create(SemesterRequestDTO dto, @Context UriInfo uriInfo) {
        SemesterResponseDTO created = semesterService.createSemester(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, SemesterRequestDTO dto) {
        SemesterResponseDTO updated = semesterService.updateSemester(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        semesterService.deleteSemester(id);
        return Response.noContent().build();
    }
}
