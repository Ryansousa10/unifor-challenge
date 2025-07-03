package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.dto.CurriculumRequestDTO;
import br.com.unifor.dto.CurriculumResponseDTO;
import br.com.unifor.service.CurriculumService;
import jakarta.inject.Inject;

@Path("/curriculum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CurriculumResource {

    // Recurso REST para gerenciamento de matrizes curriculares.
    // Cada matriz é composta por um curso e um semestre.
    //
    // Permissões:
    // - Listagem e consulta: COORDENADOR, PROFESSOR, ALUNO, ADMIN
    // - Criação, atualização e remoção: COORDENADOR, ADMIN
    //
    // Observações:
    // - IDs são gerados automaticamente como UUID
    // - Autenticação e autorização via Keycloak

    @Inject
    CurriculumService curriculumService;

    @GET
    @RolesAllowed({"COORDENADOR", "PROFESSOR", "ALUNO", "ADMIN"})
    public List<CurriculumResponseDTO> list() {
        return curriculumService.listCurriculums();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "PROFESSOR", "ALUNO", "ADMIN"})
    public Response get(@PathParam("id") UUID id) {
        CurriculumResponseDTO dto = curriculumService.getCurriculum(id);
        return Response.ok(dto).build();
    }

    @POST
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response create(CurriculumRequestDTO dto, @Context UriInfo uriInfo) {
        CurriculumResponseDTO created = curriculumService.createCurriculum(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response update(@PathParam("id") UUID id, CurriculumRequestDTO dto) {
        CurriculumResponseDTO updated = curriculumService.updateCurriculum(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response delete(@PathParam("id") UUID id) {
        curriculumService.deleteCurriculum(id);
        return Response.noContent().build();
    }
}