package br.com.unifor.rest;

import br.com.unifor.dto.DisciplineRequestDTO;
import br.com.unifor.dto.DisciplineResponseDTO;
import br.com.unifor.service.DisciplineService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class DisciplineResourceTest {

    @Inject
    DisciplineResource disciplineResource;

    @InjectMock
    DisciplineService disciplineService;

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testListDisciplines() {
        DisciplineResponseDTO d1 = new DisciplineResponseDTO();
        d1.setId(UUID.randomUUID());
        d1.setCode("INF001");
        d1.setName("Algoritmos");
        d1.setCredits(4);
        d1.setDescription("Introdução à lógica de programação");
        DisciplineResponseDTO d2 = new DisciplineResponseDTO();
        d2.setId(UUID.randomUUID());
        d2.setCode("INF002");
        d2.setName("Estruturas de Dados");
        d2.setCredits(4);
        d2.setDescription("Listas, filas, pilhas, árvores");
        List<DisciplineResponseDTO> disciplines = Arrays.asList(d1, d2);
        Mockito.when(disciplineService.listDisciplines()).thenReturn(disciplines);

        List<DisciplineResponseDTO> result = disciplineResource.list();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("INF001", result.get(0).getCode());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testGetDiscipline() {
        UUID id = UUID.randomUUID();
        DisciplineResponseDTO d = new DisciplineResponseDTO();
        d.setId(id);
        d.setCode("INF001");
        d.setName("Algoritmos");
        d.setCredits(4);
        d.setDescription("Introdução à lógica de programação");
        Mockito.when(disciplineService.getDiscipline(id)).thenReturn(d);

        Response response = disciplineResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        DisciplineResponseDTO result = (DisciplineResponseDTO) response.getEntity();
        Assertions.assertEquals("INF001", result.getCode());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testCreateDiscipline() {
        DisciplineRequestDTO request = new DisciplineRequestDTO();
        request.setCode("INF003");
        request.setName("Banco de Dados");
        request.setCredits(4);
        request.setDescription("Modelagem e SQL");
        DisciplineResponseDTO created = new DisciplineResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        created.setCode("INF003");
        created.setName("Banco de Dados");
        created.setCredits(4);
        created.setDescription("Modelagem e SQL");
        Mockito.when(disciplineService.createDiscipline(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/discipline/" + id);
            }
            return invocation.callRealMethod();
        }));

        Response response = disciplineResource.create(request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/discipline/" + id, response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testUpdateDiscipline() {
        UUID id = UUID.randomUUID();
        DisciplineRequestDTO request = new DisciplineRequestDTO();
        request.setCode("INF004");
        request.setName("Engenharia de Software");
        request.setCredits(4);
        request.setDescription("Processos de desenvolvimento");
        DisciplineResponseDTO updated = new DisciplineResponseDTO();
        updated.setId(id);
        updated.setCode("INF004");
        updated.setName("Engenharia de Software");
        updated.setCredits(4);
        updated.setDescription("Processos de desenvolvimento");
        Mockito.when(disciplineService.updateDiscipline(id, request)).thenReturn(updated);

        Response response = disciplineResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        DisciplineResponseDTO result = (DisciplineResponseDTO) response.getEntity();
        Assertions.assertEquals("INF004", result.getCode());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testDeleteDiscipline() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(disciplineService).deleteDiscipline(id);

        Response response = disciplineResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}

