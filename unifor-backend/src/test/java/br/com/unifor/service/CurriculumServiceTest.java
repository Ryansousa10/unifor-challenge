package br.com.unifor.service;

import br.com.unifor.domain.Course;
import br.com.unifor.domain.Curriculum;
import br.com.unifor.domain.Semester;
import br.com.unifor.dto.CurriculumRequestDTO;
import br.com.unifor.dto.CurriculumResponseDTO;
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
class CurriculumServiceTest {

    CurriculumService service = new CurriculumService();

    @Test
    void testListCurriculums() {
        PanacheMock.mock(Curriculum.class);

        Course course = new Course();
        course.setId(UUID.randomUUID());

        Semester semester = new Semester();
        semester.setId(UUID.randomUUID());

        Curriculum c1 = new Curriculum();
        c1.setId(UUID.randomUUID());
        c1.setCourse(course);
        c1.setSemester(semester);

        List<Curriculum> list = Arrays.asList(c1);
        Mockito.when(Curriculum.listAll()).thenReturn((List) list);

        List<CurriculumResponseDTO> result = service.listCurriculums();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(c1.getId(), result.get(0).getId());
        Assertions.assertEquals(course.getId(), result.get(0).getCourseId());
        Assertions.assertEquals(semester.getId(), result.get(0).getSemesterId());

        PanacheMock.reset();
    }

    @Test
    void testGetCurriculumFound() {
        PanacheMock.mock(Curriculum.class);
        UUID id = UUID.randomUUID();

        Course course = new Course();
        course.setId(UUID.randomUUID());

        Semester semester = new Semester();
        semester.setId(UUID.randomUUID());

        Curriculum curriculum = new Curriculum();
        curriculum.setId(id);
        curriculum.setCourse(course);
        curriculum.setSemester(semester);

        Mockito.when(Curriculum.findById(id)).thenReturn(curriculum);

        CurriculumResponseDTO result = service.getCurriculum(id);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(course.getId(), result.getCourseId());
        Assertions.assertEquals(semester.getId(), result.getSemesterId());

        PanacheMock.reset();
    }

    @Test
    void testGetCurriculumNotFound() {
        PanacheMock.mock(Curriculum.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Curriculum.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.getCurriculum(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateCurriculumSuccess() {
        PanacheMock.mock(Curriculum.class);
        PanacheMock.mock(Course.class);
        PanacheMock.mock(Semester.class);

        UUID courseId = UUID.randomUUID();
        UUID semesterId = UUID.randomUUID();

        Course course = new Course();
        course.setId(courseId);

        Semester semester = new Semester();
        semester.setId(semesterId);

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);
        dto.setSemesterId(semesterId);

        Mockito.when(Course.findById(courseId)).thenReturn(course);
        Mockito.when(Semester.findById(semesterId)).thenReturn(semester);

        try (var mocked = Mockito.mockConstruction(Curriculum.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            UUID id = UUID.randomUUID();
            Mockito.when(mock.getId()).thenReturn(id);
            Mockito.when(mock.getCourse()).thenReturn(course);
            Mockito.when(mock.getSemester()).thenReturn(semester);
        })) {
            CurriculumResponseDTO response = service.createCurriculum(dto);
            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(courseId, response.getCourseId());
            Assertions.assertEquals(semesterId, response.getSemesterId());
        }

        PanacheMock.reset();
    }

    @Test
    void testCreateCurriculumCourseNotFound() {
        PanacheMock.mock(Course.class);
        UUID courseId = UUID.randomUUID();

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);

        Mockito.when(Course.findById(courseId)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.createCurriculum(dto));
        PanacheMock.reset();
    }

    @Test
    void testCreateCurriculumSemesterNotFound() {
        PanacheMock.mock(Course.class);
        PanacheMock.mock(Semester.class);

        UUID courseId = UUID.randomUUID();
        UUID semesterId = UUID.randomUUID();

        Course course = new Course();
        course.setId(courseId);

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);
        dto.setSemesterId(semesterId);

        Mockito.when(Course.findById(courseId)).thenReturn(course);
        Mockito.when(Semester.findById(semesterId)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.createCurriculum(dto));
        PanacheMock.reset();
    }

    @Test
    void testUpdateCurriculumSuccess() {
        PanacheMock.mock(Curriculum.class);
        PanacheMock.mock(Course.class);
        PanacheMock.mock(Semester.class);

        UUID id = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID semesterId = UUID.randomUUID();

        Course course = new Course();
        course.setId(courseId);

        Semester semester = new Semester();
        semester.setId(semesterId);

        Curriculum existing = new Curriculum();
        existing.setId(id);

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);
        dto.setSemesterId(semesterId);

        Mockito.when(Curriculum.findById(id)).thenReturn(existing);
        Mockito.when(Course.findById(courseId)).thenReturn(course);
        Mockito.when(Semester.findById(semesterId)).thenReturn(semester);

        CurriculumResponseDTO response = service.updateCurriculum(id, dto);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals(courseId, response.getCourseId());
        Assertions.assertEquals(semesterId, response.getSemesterId());

        PanacheMock.reset();
    }

    @Test
    void testUpdateCurriculumNotFound() {
        PanacheMock.mock(Curriculum.class);
        UUID id = UUID.randomUUID();

        CurriculumRequestDTO dto = new CurriculumRequestDTO();

        Mockito.when(Curriculum.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateCurriculum(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testUpdateCurriculumCourseNotFound() {
        PanacheMock.mock(Curriculum.class);
        PanacheMock.mock(Course.class);

        UUID id = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        Curriculum existing = new Curriculum();
        existing.setId(id);

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);

        Mockito.when(Curriculum.findById(id)).thenReturn(existing);
        Mockito.when(Course.findById(courseId)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateCurriculum(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testUpdateCurriculumSemesterNotFound() {
        PanacheMock.mock(Curriculum.class);
        PanacheMock.mock(Course.class);
        PanacheMock.mock(Semester.class);

        UUID id = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID semesterId = UUID.randomUUID();

        Curriculum existing = new Curriculum();
        existing.setId(id);

        Course course = new Course();
        course.setId(courseId);

        CurriculumRequestDTO dto = new CurriculumRequestDTO();
        dto.setCourseId(courseId);
        dto.setSemesterId(semesterId);

        Mockito.when(Curriculum.findById(id)).thenReturn(existing);
        Mockito.when(Course.findById(courseId)).thenReturn(course);
        Mockito.when(Semester.findById(semesterId)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateCurriculum(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCurriculumSuccess() {
        PanacheMock.mock(Curriculum.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Curriculum.deleteById(id)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.deleteCurriculum(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCurriculumNotFound() {
        PanacheMock.mock(Curriculum.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Curriculum.deleteById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteCurriculum(id));
        PanacheMock.reset();
    }
}
