package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.domain.Discipline;



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

    @GET
    public List<Discipline> list() {
        return Discipline.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        Discipline d = Discipline.findById(id);
        return d != null
                ? Response.ok(d).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(Discipline discipline, @Context UriInfo uriInfo) {
        discipline.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(discipline.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(@PathParam("id") UUID id, Discipline updated) {
        Discipline existing = Discipline.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setCredits(updated.getCredits());
        existing.setDescription(updated.getDescription());
        return Response.ok(existing).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Discipline.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}