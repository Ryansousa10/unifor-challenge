package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.dto.DisciplineRequestDTO;
import br.com.unifor.dto.DisciplineResponseDTO;
import br.com.unifor.service.DisciplineService;
import jakarta.inject.Inject;



@Path("/discipline")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplineResource {

    // Recurso REST para gerenciamento de disciplinas.
    // Controla o cadastro e manutenção das disciplinas oferecidas.
    //
    // Decisões técnicas:
    // - Campo 'code' é único e representa o código da disciplina
    // - Campos obrigatórios: nome, carga horária e créditos
    //
    // Permissões:
    // - Todas as operações restritas a COORDENADOR e ADMIN
    //
    // Observações:
    // - IDs são gerados automaticamente como UUID
    // - Autenticação e autorização via Keycloak

    @Inject
    DisciplineService disciplineService;

    @GET
    @RolesAllowed({"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    public List<DisciplineResponseDTO> list() {
        return disciplineService.listDisciplines();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    public Response get(@PathParam("id") UUID id) {
        DisciplineResponseDTO dto = disciplineService.getDiscipline(id);
        return Response.ok(dto).build();
    }

    @POST
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response create(DisciplineRequestDTO dto, @Context UriInfo uriInfo) {
        DisciplineResponseDTO created = disciplineService.createDiscipline(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    public Response update(@PathParam("id") UUID id, DisciplineRequestDTO dto) {
        DisciplineResponseDTO updated = disciplineService.updateDiscipline(id, dto);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        disciplineService.deleteDiscipline(id);
        return Response.noContent().build();
    }
}