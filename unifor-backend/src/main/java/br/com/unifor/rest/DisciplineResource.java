package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
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
@RolesAllowed({"COORDENADOR", "ADMIN"})
public class DisciplineResource {

    // Recurso REST responsável pelo gerenciamento de disciplinas da instituição.
    // Disponível apenas para usuários com perfil COORDENADOR ou ADMIN, conforme @RolesAllowed.
    // Implementa operações CRUD básicas para a entidade Discipline.
    //
    // Decisão: O campo 'code' é considerado único e representa o código institucional da disciplina.
    // O campo 'credits' é obrigatório e representa a carga horária ou créditos da disciplina.
    // O campo 'description' é opcional e pode detalhar a disciplina.
    // O método persist() do Panache cuida da geração do UUID da disciplina.
    // O método deleteById retorna true se a disciplina foi removida, false se não encontrada.
    // O método update atualiza todos os campos editáveis da disciplina, exceto o id.
    // O endpoint está protegido por autenticação e autorização via Keycloak.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @Inject
    DisciplineService disciplineService;

    @GET
    public List<DisciplineResponseDTO> list() {
        return disciplineService.listDisciplines();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        DisciplineResponseDTO dto = disciplineService.getDiscipline(id);
        return Response.ok(dto).build();
    }

    @POST
    public Response create(DisciplineRequestDTO dto, @Context UriInfo uriInfo) {
        DisciplineResponseDTO created = disciplineService.createDiscipline(dto);
        URI uri = uriInfo.getAbsolutePathBuilder().path(created.getId().toString()).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("{id}")
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