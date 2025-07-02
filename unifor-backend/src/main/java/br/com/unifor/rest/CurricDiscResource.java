package br.com.unifor.rest;

import br.com.unifor.domain.CurricDiscId;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.domain.CurricDisc;

@Path("/curriculum/{curriculumId}/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"COORDENADOR", "ADMIN"})
public class CurricDiscResource {

    // Recurso REST responsável pelo gerenciamento da associação entre matriz curricular e disciplinas (CurricDisc).
    // Disponível apenas para usuários com perfil COORDENADOR ou ADMIN, conforme @RolesAllowed.
    // Permite listar, adicionar, atualizar e remover disciplinas de uma matriz curricular específica.
    //
    // Decisão: A associação é identificada por chave composta (curriculumId + disciplineId).
    // O método create vincula uma disciplina a uma matriz curricular, exigindo o curriculumId na URL.
    // O método update permite alterar a ordem (ordering) da disciplina na matriz.
    // O método delete remove a associação entre a matriz curricular e a disciplina.
    // O endpoint está protegido por autenticação e autorização via Keycloak.
    // O padrão REST facilita integração com frontend e outros sistemas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    @GET
    public List<CurricDisc> list(@PathParam("curriculumId") UUID curriculumId) {
        return CurricDisc.list("curriculumId", curriculumId);
    }

    @POST
    @Transactional
    public Response create(@PathParam("curriculumId") UUID curriculumId,
                           CurricDisc disc, @Context UriInfo uriInfo) {
        disc.setCurriculumId(curriculumId);
        disc.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(disc.getDisciplineId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{disciplineId}")
    @Transactional
    public Response update(@PathParam("curriculumId") UUID curriculumId,
                           @PathParam("disciplineId") UUID disciplineId,
                           CurricDisc updated) {
        CurricDiscId key = new CurricDiscId(curriculumId, disciplineId);
        CurricDisc existing = CurricDisc.findById(key);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setOrdering(updated.getOrdering());
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("{disciplineId}")
    @Transactional
    public Response delete(@PathParam("curriculumId") UUID curriculumId,
                           @PathParam("disciplineId") UUID disciplineId) {
        CurricDiscId key = new CurricDiscId(curriculumId, disciplineId);
        boolean deleted = CurricDisc.deleteById(key);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}