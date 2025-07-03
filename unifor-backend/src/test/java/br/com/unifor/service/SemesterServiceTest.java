package br.com.unifor.service;

import br.com.unifor.domain.Semester;
import br.com.unifor.dto.SemesterRequestDTO;
import br.com.unifor.dto.SemesterResponseDTO;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class SemesterServiceTest {

    SemesterService service = new SemesterService();

    @Test
    void testListSemesters() {
        PanacheMock.mock(Semester.class);

        Semester s1 = new Semester();
        s1.setId(UUID.randomUUID());
        s1.setName("2025.1");
        s1.setStartDate(LocalDate.of(2025, 1, 1));
        s1.setEndDate(LocalDate.of(2025, 6, 30));

        List<Semester> list = Arrays.asList(s1);
        Mockito.when(Semester.listAll()).thenReturn((List) list);

        List<SemesterResponseDTO> result = service.listSemesters();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(s1.getId(), result.get(0).getId());
        Assertions.assertEquals(s1.getName(), result.get(0).getName());
        Assertions.assertEquals(s1.getStartDate(), result.get(0).getStartDate());
        Assertions.assertEquals(s1.getEndDate(), result.get(0).getEndDate());

        PanacheMock.reset();
    }

    @Test
    void testGetSemesterFound() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        Semester semester = new Semester();
        semester.setId(id);
        semester.setName("2025.1");
        semester.setStartDate(LocalDate.of(2025, 1, 1));
        semester.setEndDate(LocalDate.of(2025, 6, 30));

        Mockito.when(Semester.findById(id)).thenReturn(semester);

        SemesterResponseDTO result = service.getSemester(id);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(semester.getName(), result.getName());
        Assertions.assertEquals(semester.getStartDate(), result.getStartDate());
        Assertions.assertEquals(semester.getEndDate(), result.getEndDate());

        PanacheMock.reset();
    }

    @Test
    void testGetSemesterNotFound() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Semester.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.getSemester(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateSemesterSuccess() {
        PanacheMock.mock(Semester.class);

        SemesterRequestDTO dto = new SemesterRequestDTO();
        dto.setName("2025.1");
        dto.setStartDate(LocalDate.of(2025, 1, 1));
        dto.setEndDate(LocalDate.of(2025, 6, 30));

        try (var mocked = Mockito.mockConstruction(Semester.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            UUID id = UUID.randomUUID();
            Mockito.when(mock.getId()).thenReturn(id);
            Mockito.when(mock.getName()).thenReturn(dto.getName());
            Mockito.when(mock.getStartDate()).thenReturn(dto.getStartDate());
            Mockito.when(mock.getEndDate()).thenReturn(dto.getEndDate());
        })) {
            SemesterResponseDTO response = service.createSemester(dto);
            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(dto.getName(), response.getName());
            Assertions.assertEquals(dto.getStartDate(), response.getStartDate());
            Assertions.assertEquals(dto.getEndDate(), response.getEndDate());
        }

        PanacheMock.reset();
    }

    @Test
    void testUpdateSemesterSuccess() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        Semester existing = new Semester();
        existing.setId(id);
        existing.setName("2025.1");
        existing.setStartDate(LocalDate.of(2025, 1, 1));
        existing.setEndDate(LocalDate.of(2025, 6, 30));

        SemesterRequestDTO dto = new SemesterRequestDTO();
        dto.setName("2025.2");
        dto.setStartDate(LocalDate.of(2025, 7, 1));
        dto.setEndDate(LocalDate.of(2025, 12, 31));

        Mockito.when(Semester.findById(id)).thenReturn(existing);

        SemesterResponseDTO response = service.updateSemester(id, dto);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals(dto.getName(), response.getName());
        Assertions.assertEquals(dto.getStartDate(), response.getStartDate());
        Assertions.assertEquals(dto.getEndDate(), response.getEndDate());

        PanacheMock.reset();
    }

    @Test
    void testUpdateSemesterNotFound() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        SemesterRequestDTO dto = new SemesterRequestDTO();
        dto.setName("2025.1");
        dto.setStartDate(LocalDate.of(2025, 1, 1));
        dto.setEndDate(LocalDate.of(2025, 6, 30));

        Mockito.when(Semester.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateSemester(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteSemesterSuccess() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Semester.deleteById(id)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.deleteSemester(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteSemesterNotFound() {
        PanacheMock.mock(Semester.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Semester.deleteById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteSemester(id));
        PanacheMock.reset();
    }
}
