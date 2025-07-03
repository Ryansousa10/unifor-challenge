package br.com.unifor.service;

import br.com.unifor.domain.Discipline;
import br.com.unifor.dto.DisciplineRequestDTO;
import br.com.unifor.dto.DisciplineResponseDTO;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class DisciplineServiceTest {

    DisciplineService service = new DisciplineService();

    @Test
    void testListDisciplines() {
        PanacheMock.mock(Discipline.class);

        Discipline d1 = new Discipline();
        d1.setId(UUID.randomUUID());
        d1.setCode("MAT001");
        d1.setName("Matemática");
        d1.setCredits(4);
        d1.setDescription("Matemática Básica");

        List<Discipline> list = Arrays.asList(d1);
        Mockito.when(Discipline.listAll()).thenReturn((List) list);

        List<DisciplineResponseDTO> result = service.listDisciplines();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(d1.getId(), result.get(0).getId());
        Assertions.assertEquals(d1.getCode(), result.get(0).getCode());
        Assertions.assertEquals(d1.getName(), result.get(0).getName());
        Assertions.assertEquals(d1.getCredits(), result.get(0).getCredits());
        Assertions.assertEquals(d1.getDescription(), result.get(0).getDescription());

        PanacheMock.reset();
    }

    @Test
    void testGetDisciplineFound() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        Discipline discipline = new Discipline();
        discipline.setId(id);
        discipline.setCode("MAT001");
        discipline.setName("Matemática");
        discipline.setCredits(4);
        discipline.setDescription("Matemática Básica");

        Mockito.when(Discipline.findById(id)).thenReturn(discipline);

        DisciplineResponseDTO result = service.getDiscipline(id);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(discipline.getCode(), result.getCode());
        Assertions.assertEquals(discipline.getName(), result.getName());
        Assertions.assertEquals(discipline.getCredits(), result.getCredits());
        Assertions.assertEquals(discipline.getDescription(), result.getDescription());

        PanacheMock.reset();
    }

    @Test
    void testGetDisciplineNotFound() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Discipline.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.getDiscipline(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateDisciplineSuccess() {
        PanacheMock.mock(Discipline.class);

        DisciplineRequestDTO dto = new DisciplineRequestDTO();
        dto.setCode("MAT001");
        dto.setName("Matemática");
        dto.setCredits(4);
        dto.setDescription("Matemática Básica");

        try (var mocked = Mockito.mockConstruction(Discipline.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            UUID id = UUID.randomUUID();
            Mockito.when(mock.getId()).thenReturn(id);
            Mockito.when(mock.getCode()).thenReturn(dto.getCode());
            Mockito.when(mock.getName()).thenReturn(dto.getName());
            Mockito.when(mock.getCredits()).thenReturn(dto.getCredits());
            Mockito.when(mock.getDescription()).thenReturn(dto.getDescription());
        })) {
            DisciplineResponseDTO response = service.createDiscipline(dto);
            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(dto.getCode(), response.getCode());
            Assertions.assertEquals(dto.getName(), response.getName());
            Assertions.assertEquals(dto.getCredits(), response.getCredits());
            Assertions.assertEquals(dto.getDescription(), response.getDescription());
        }

        PanacheMock.reset();
    }

    @Test
    void testUpdateDisciplineSuccess() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        Discipline existing = new Discipline();
        existing.setId(id);
        existing.setCode("MAT001");
        existing.setName("Matemática");
        existing.setCredits(4);
        existing.setDescription("Matemática Básica");

        DisciplineRequestDTO dto = new DisciplineRequestDTO();
        dto.setCode("MAT002");
        dto.setName("Matemática Avançada");
        dto.setCredits(6);
        dto.setDescription("Matemática Avançada");

        Mockito.when(Discipline.findById(id)).thenReturn(existing);

        DisciplineResponseDTO response = service.updateDiscipline(id, dto);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals(dto.getCode(), response.getCode());
        Assertions.assertEquals(dto.getName(), response.getName());
        Assertions.assertEquals(dto.getCredits(), response.getCredits());
        Assertions.assertEquals(dto.getDescription(), response.getDescription());

        PanacheMock.reset();
    }

    @Test
    void testUpdateDisciplineNotFound() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        DisciplineRequestDTO dto = new DisciplineRequestDTO();
        dto.setCode("MAT001");
        dto.setName("Matemática");
        dto.setCredits(4);
        dto.setDescription("Matemática Básica");

        Mockito.when(Discipline.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateDiscipline(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteDisciplineSuccess() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Discipline.deleteById(id)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.deleteDiscipline(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteDisciplineNotFound() {
        PanacheMock.mock(Discipline.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Discipline.deleteById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteDiscipline(id));
        PanacheMock.reset();
    }
}
