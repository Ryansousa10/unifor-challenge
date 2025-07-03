package br.com.unifor.rest;

import br.com.unifor.dto.CurricDiscRequestDTO;
import br.com.unifor.dto.CurricDiscResponseDTO;
import br.com.unifor.service.CurricDiscService;
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
class CurricDiscResourceTest {

    @Inject
    CurricDiscResource curricDiscResource;

    @InjectMock
    CurricDiscService curricDiscService;

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testListCurricDiscs() {
        UUID curriculumId = UUID.randomUUID();
        CurricDiscResponseDTO cd1 = new CurricDiscResponseDTO();
        cd1.setCurriculumId(curriculumId);
        cd1.setDisciplineId(UUID.randomUUID());
        cd1.setOrdering(1);
        CurricDiscResponseDTO cd2 = new CurricDiscResponseDTO();
        cd2.setCurriculumId(curriculumId);
        cd2.setDisciplineId(UUID.randomUUID());
        cd2.setOrdering(2);
        List<CurricDiscResponseDTO> all = Arrays.asList(cd1, cd2);
        Mockito.when(curricDiscService.listCurricDiscs()).thenReturn(all);

        List<CurricDiscResponseDTO> result = curricDiscResource.list(curriculumId);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(curriculumId, result.get(0).getCurriculumId());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testGetCurricDisc() {
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDiscResponseDTO cd = new CurricDiscResponseDTO();
        cd.setCurriculumId(curriculumId);
        cd.setDisciplineId(disciplineId);
        cd.setOrdering(1);
        Mockito.when(curricDiscService.getCurricDisc(curriculumId, disciplineId)).thenReturn(cd);

        Response response = curricDiscResource.get(curriculumId, disciplineId);
        Assertions.assertEquals(200, response.getStatus());
        CurricDiscResponseDTO result = (CurricDiscResponseDTO) response.getEntity();
        Assertions.assertEquals(curriculumId, result.getCurriculumId());
        Assertions.assertEquals(disciplineId, result.getDisciplineId());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testCreateCurricDisc() {
        UUID curriculumId = UUID.randomUUID();
        CurricDiscRequestDTO request = new CurricDiscRequestDTO();
        request.setDisciplineId(UUID.randomUUID());
        request.setOrdering(1);
        CurricDiscResponseDTO created = new CurricDiscResponseDTO();
        created.setCurriculumId(curriculumId);
        created.setDisciplineId(request.getDisciplineId());
        created.setOrdering(1);
        Mockito.when(curricDiscService.createCurricDisc(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/curriculum/" + curriculumId + "/disciplines/" + request.getDisciplineId());
            }
            return invocation.callRealMethod();
        }));

        Response response = curricDiscResource.create(curriculumId, request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/curriculum/" + curriculumId + "/disciplines/" + request.getDisciplineId(), response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testUpdateCurricDisc() {
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDiscRequestDTO request = new CurricDiscRequestDTO();
        request.setOrdering(2);
        CurricDiscResponseDTO updated = new CurricDiscResponseDTO();
        updated.setCurriculumId(curriculumId);
        updated.setDisciplineId(disciplineId);
        updated.setOrdering(2);
        Mockito.when(curricDiscService.updateCurricDisc(curriculumId, disciplineId, request)).thenReturn(updated);

        Response response = curricDiscResource.update(curriculumId, disciplineId, request);
        Assertions.assertEquals(200, response.getStatus());
        CurricDiscResponseDTO result = (CurricDiscResponseDTO) response.getEntity();
        Assertions.assertEquals(2, result.getOrdering());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testDeleteCurricDisc() {
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        Mockito.doNothing().when(curricDiscService).deleteCurricDisc(curriculumId, disciplineId);

        Response response = curricDiscResource.delete(curriculumId, disciplineId);
        Assertions.assertEquals(204, response.getStatus());
    }
}
