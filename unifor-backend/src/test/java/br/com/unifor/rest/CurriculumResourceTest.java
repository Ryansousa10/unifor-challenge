package br.com.unifor.rest;

import br.com.unifor.dto.CurriculumRequestDTO;
import br.com.unifor.dto.CurriculumResponseDTO;
import br.com.unifor.service.CurriculumService;
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
class CurriculumResourceTest {

    @Inject
    CurriculumResource curriculumResource;

    @InjectMock
    CurriculumService curriculumService;

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    void testListCurriculums() {
        CurriculumResponseDTO c1 = new CurriculumResponseDTO();
        c1.setId(UUID.randomUUID());
        c1.setCourseId(UUID.randomUUID());
        c1.setSemesterId(UUID.randomUUID());
        CurriculumResponseDTO c2 = new CurriculumResponseDTO();
        c2.setId(UUID.randomUUID());
        c2.setCourseId(UUID.randomUUID());
        c2.setSemesterId(UUID.randomUUID());
        List<CurriculumResponseDTO> curriculums = Arrays.asList(c1, c2);
        Mockito.when(curriculumService.listCurriculums()).thenReturn(curriculums);

        List<CurriculumResponseDTO> result = curriculumResource.list();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN", "PROFESSOR", "ALUNO"})
    void testGetCurriculum() {
        UUID id = UUID.randomUUID();
        CurriculumResponseDTO c = new CurriculumResponseDTO();
        c.setId(id);
        c.setCourseId(UUID.randomUUID());
        c.setSemesterId(UUID.randomUUID());
        Mockito.when(curriculumService.getCurriculum(id)).thenReturn(c);

        Response response = curriculumResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        CurriculumResponseDTO result = (CurriculumResponseDTO) response.getEntity();
        Assertions.assertEquals(id, result.getId());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testCreateCurriculum() {
        CurriculumRequestDTO request = new CurriculumRequestDTO();
        request.setCourseId(UUID.randomUUID());
        request.setSemesterId(UUID.randomUUID());
        CurriculumResponseDTO created = new CurriculumResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        created.setCourseId(request.getCourseId());
        created.setSemesterId(request.getSemesterId());
        Mockito.when(curriculumService.createCurriculum(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/curriculum/" + id);
            }
            return invocation.callRealMethod();
        }));

        Response response = curriculumResource.create(request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/curriculum/" + id, response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testUpdateCurriculum() {
        UUID id = UUID.randomUUID();
        CurriculumRequestDTO request = new CurriculumRequestDTO();
        request.setCourseId(UUID.randomUUID());
        request.setSemesterId(UUID.randomUUID());
        CurriculumResponseDTO updated = new CurriculumResponseDTO();
        updated.setId(id);
        updated.setCourseId(request.getCourseId());
        updated.setSemesterId(request.getSemesterId());
        Mockito.when(curriculumService.updateCurriculum(id, request)).thenReturn(updated);

        Response response = curriculumResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        CurriculumResponseDTO result = (CurriculumResponseDTO) response.getEntity();
        Assertions.assertEquals(id, result.getId());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testDeleteCurriculum() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(curriculumService).deleteCurriculum(id);

        Response response = curriculumResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}

