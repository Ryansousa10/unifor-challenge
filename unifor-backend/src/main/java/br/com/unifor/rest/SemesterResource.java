package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
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
public class SemesterResource {

    // Recurso REST para gerenciamento de semestres letivos.
    // Controla o cadastro e manutenção dos semestres acadêmicos.
    //
    // Decisões técnicas:
    // - Campo 'code' é único e representa o código do semestre (ex: 2023.1)
    // - Campo 'description' é opcional para informações adicionais
    //
    // Permissões:
    // - Todas as operações restritas a COORDENADOR e ADMIN
    //
    // Observações:
    // - IDs são gerados automaticamente como UUID
    // - Autenticação e autorização via Keycloak

    @Inject
    SemesterService semesterService;

    @GET
    @RolesAllowed({"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    public List<SemesterResponseDTO> list() {
        return semesterService.listSemesters();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    public Response get(@PathParam("id") UUID id) {
        SemesterResponseDTO dto = semesterService.getSemester(id);
        return Response.ok(dto).build();
    }

    @POST
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response create(SemesterRequestDTO dto, @Context UriInfo uriInfo) {
        SemesterResponseDTO created = semesterService.createSemester(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
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
