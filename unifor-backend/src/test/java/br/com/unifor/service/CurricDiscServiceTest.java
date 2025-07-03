package br.com.unifor.service;

import br.com.unifor.domain.CurricDisc;
import br.com.unifor.dto.CurricDiscRequestDTO;
import br.com.unifor.dto.CurricDiscResponseDTO;
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
class CurricDiscServiceTest {

    CurricDiscService service = new CurricDiscService();

    @Test
    void testListCurricDiscs() {
        PanacheMock.mock(CurricDisc.class);
        CurricDisc c1 = new CurricDisc();
        c1.setCurriculumId(UUID.randomUUID());
        c1.setDisciplineId(UUID.randomUUID());
        c1.setOrdering(1);
        CurricDisc c2 = new CurricDisc();
        c2.setCurriculumId(UUID.randomUUID());
        c2.setDisciplineId(UUID.randomUUID());
        c2.setOrdering(2);
        List<CurricDisc> list = Arrays.asList(c1, c2);
        Mockito.when(CurricDisc.listAll()).thenReturn((List) list);
        List<CurricDiscResponseDTO> result = service.listCurricDiscs();
        Assertions.assertEquals(2, result.size());
        PanacheMock.reset();
    }

    @Test
    void testGetCurricDiscFound() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDisc c = new CurricDisc();
        c.setCurriculumId(curriculumId);
        c.setDisciplineId(disciplineId);
        c.setOrdering(1);
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(c);

        CurricDiscResponseDTO result = service.getCurricDisc(curriculumId, disciplineId);
        Assertions.assertEquals(curriculumId, result.getCurriculumId());
        Assertions.assertEquals(disciplineId, result.getDisciplineId());
        PanacheMock.reset();
    }

    @Test
    void testGetCurricDiscNotFound() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.getCurricDisc(curriculumId, disciplineId));
        PanacheMock.reset();
    }

    @Test
    void testCreateCurricDisc() {
        PanacheMock.mock(CurricDisc.class);
        CurricDiscRequestDTO dto = new CurricDiscRequestDTO();
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        dto.setCurriculumId(curriculumId);
        dto.setDisciplineId(disciplineId);
        dto.setOrdering(1);
        try (var mocked = Mockito.mockConstruction(CurricDisc.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            Mockito.when(mock.getCurriculumId()).thenReturn(curriculumId);
            Mockito.when(mock.getDisciplineId()).thenReturn(disciplineId);
            Mockito.when(mock.getOrdering()).thenReturn(1);
        })) {
            CurricDiscResponseDTO response = service.createCurricDisc(dto);
            Assertions.assertEquals(curriculumId, response.getCurriculumId());
            Assertions.assertEquals(disciplineId, response.getDisciplineId());
            Assertions.assertEquals(1, response.getOrdering());
        }
        PanacheMock.reset();
    }

    @Test
    void testUpdateCurricDiscFound() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDiscRequestDTO dto = new CurricDiscRequestDTO();
        dto.setCurriculumId(curriculumId);
        dto.setDisciplineId(disciplineId);
        dto.setOrdering(2);
        CurricDisc existing = new CurricDisc();
        existing.setCurriculumId(curriculumId);
        existing.setDisciplineId(disciplineId);
        existing.setOrdering(1);
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(existing);

        CurricDiscResponseDTO response = service.updateCurricDisc(curriculumId, disciplineId, dto);
        Assertions.assertEquals(2, response.getOrdering());
        PanacheMock.reset();
    }

    @Test
    void testUpdateCurricDiscNotFound() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDiscRequestDTO dto = new CurricDiscRequestDTO();
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateCurricDisc(curriculumId, disciplineId, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCurricDiscSuccess() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        CurricDisc existing = Mockito.mock(CurricDisc.class);
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(existing);

        Mockito.doNothing().when(existing).delete();
        Assertions.assertDoesNotThrow(() -> service.deleteCurricDisc(curriculumId, disciplineId));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCurricDiscNotFound() {
        PanacheMock.mock(CurricDisc.class);
        UUID curriculumId = UUID.randomUUID();
        UUID disciplineId = UUID.randomUUID();
        var query = Mockito.mock(io.quarkus.hibernate.orm.panache.PanacheQuery.class);

        Object[] params = new Object[]{curriculumId, disciplineId};
        Mockito.when(CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", params)).thenReturn(query);
        Mockito.when(query.firstResult()).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteCurricDisc(curriculumId, disciplineId));
        PanacheMock.reset();
    }
}
