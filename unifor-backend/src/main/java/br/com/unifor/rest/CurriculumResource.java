package br.com.unifor.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import br.com.unifor.domain.Curriculum;

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

    @GET
    @RolesAllowed({"COORDENADOR", "PROFESSOR", "ALUNO", "ADMIN"})
    public List<Curriculum> list() {
        return Curriculum.listAll();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "PROFESSOR", "ALUNO", "ADMIN"})
    public Response get(@PathParam("id") UUID id) {
        Curriculum c = Curriculum.findById(id);
        return c != null
                ? Response.ok(c).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    @Transactional
    public Response create(Curriculum curriculum, @Context UriInfo uriInfo) {
        curriculum.persist();
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(curriculum.getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    @Transactional
    public Response update(@PathParam("id") UUID id, Curriculum updated) {
        Curriculum existing = Curriculum.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.setCourse(updated.getCourse());
        existing.setSemester(updated.getSemester());
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"COORDENADOR", "ADMIN"})
    @Transactional
    public Response delete(@PathParam("id") UUID id) {
        boolean deleted = Curriculum.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}