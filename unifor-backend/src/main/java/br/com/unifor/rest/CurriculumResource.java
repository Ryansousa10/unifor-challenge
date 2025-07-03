package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
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

    // Recurso REST responsável pelo gerenciamento de matrizes curriculares (Curriculum).
    // Permite listar e consultar matrizes para todos os perfis (COORDENADOR, PROFESSOR, ALUNO, ADMIN).
    // Apenas usuários com perfil COORDENADOR ou ADMIN podem criar, atualizar ou remover matrizes curriculares.
    //
    // Decisão: A matriz curricular é composta por um curso e um semestre, ambos obrigatórios.
    // O método persist() do Panache cuida da geração do UUID da matriz.
    // O método deleteById retorna true se a matriz foi removida, false se não encontrada.
    // O método update atualiza os campos de curso e semestre da matriz, exceto o id.
    // O endpoint está protegido por autenticação e autorização via Keycloak.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

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