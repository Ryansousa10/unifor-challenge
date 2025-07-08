package br.com.unifor.rest;

import br.com.unifor.dto.SemesterRequestDTO;
import br.com.unifor.dto.SemesterResponseDTO;
import br.com.unifor.service.SemesterService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class SemesterResourceTest {

    @Inject
    SemesterResource semesterResource;

    @InjectMock
    SemesterService semesterService;

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testListSemesters() {
        SemesterResponseDTO s1 = new SemesterResponseDTO();
        s1.setId(UUID.randomUUID());
        s1.setName("2025.1");
        s1.setStartDate(LocalDate.of(2025, 1, 1));
        s1.setEndDate(LocalDate.of(2025, 6, 30));
        SemesterResponseDTO s2 = new SemesterResponseDTO();
        s2.setId(UUID.randomUUID());
        s2.setName("2025.2");
        s2.setStartDate(LocalDate.of(2025, 7, 1));
        s2.setEndDate(LocalDate.of(2025, 12, 31));
        List<SemesterResponseDTO> semesters = Arrays.asList(s1, s2);
        Mockito.when(semesterService.listSemesters()).thenReturn(semesters);

        List<SemesterResponseDTO> result = semesterResource.list();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("2025.1", result.get(0).getName());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testGetSemester() {
        UUID id = UUID.randomUUID();
        SemesterResponseDTO s = new SemesterResponseDTO();
        s.setId(id);
        s.setName("2025.1");
        s.setStartDate(LocalDate.of(2025, 1, 1));
        s.setEndDate(LocalDate.of(2025, 6, 30));
        Mockito.when(semesterService.getSemester(id)).thenReturn(s);

        Response response = semesterResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        SemesterResponseDTO result = (SemesterResponseDTO) response.getEntity();
        Assertions.assertEquals("2025.1", result.getName());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testCreateSemester() {
        SemesterRequestDTO request = new SemesterRequestDTO();
        request.setName("2026.1");
        request.setStartDate(LocalDate.of(2026, 1, 1));
        request.setEndDate(LocalDate.of(2026, 6, 30));
        SemesterResponseDTO created = new SemesterResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        created.setName("2026.1");
        created.setStartDate(request.getStartDate());
        created.setEndDate(request.getEndDate());
        Mockito.when(semesterService.createSemester(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/semester/" + id);
            }
            return invocation.callRealMethod();
        }));

        Response response = semesterResource.create(request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/semester/" + id, response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testUpdateSemester() {
        UUID id = UUID.randomUUID();
        SemesterRequestDTO request = new SemesterRequestDTO();
        request.setName("2026.2");
        request.setStartDate(LocalDate.of(2026, 7, 1));
        request.setEndDate(LocalDate.of(2026, 12, 31));
        SemesterResponseDTO updated = new SemesterResponseDTO();
        updated.setId(id);
        updated.setName("2026.2");
        updated.setStartDate(request.getStartDate());
        updated.setEndDate(request.getEndDate());
        Mockito.when(semesterService.updateSemester(id, request)).thenReturn(updated);

        Response response = semesterResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        SemesterResponseDTO result = (SemesterResponseDTO) response.getEntity();
        Assertions.assertEquals("2026.2", result.getName());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testDeleteSemester() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(semesterService).deleteSemester(id);

        Response response = semesterResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}
