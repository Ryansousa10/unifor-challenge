package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.domain.Semester;

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

    @GET
    public List<Semester> list() {
        return Semester.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") UUID id) {
        Semester s = Semester.findById(id);
        return s != null
                ? Response.ok(s).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(Semester semester, @Context UriInfo uriInfo) {
        semester.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(semester.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") UUID id, Semester updated) {
        Semester existing = Semester.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setName(updated.getName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Semester.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
