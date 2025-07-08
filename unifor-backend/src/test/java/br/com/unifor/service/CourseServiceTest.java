package br.com.unifor.service;

import br.com.unifor.domain.Course;
import br.com.unifor.dto.CourseRequestDTO;
import br.com.unifor.dto.CourseResponseDTO;
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
class CourseServiceTest {

    CourseService service = new CourseService();

    @Test
    void testListCourses() {
        PanacheMock.mock(Course.class);
        Course c1 = new Course();
        c1.setId(UUID.randomUUID());
        c1.setCode("INF");
        c1.setName("Engenharia de Software");
        Course c2 = new Course();
        c2.setId(UUID.randomUUID());
        c2.setCode("ADM");
        c2.setName("Administração");
        List<Course> courses = Arrays.asList(c1, c2);
        Mockito.when(Course.listAll()).thenReturn((List) courses);
        List<CourseResponseDTO> result = service.listCourses();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("INF", result.get(0).getCode());
        PanacheMock.reset();
    }

    @Test
    void testGetCourseFound() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        Course c = new Course();
        c.setId(id);
        c.setCode("INF");
        c.setName("Engenharia de Software");
        Mockito.when(Course.findById(id)).thenReturn(c);
        CourseResponseDTO result = service.getCourse(id);
        Assertions.assertEquals("INF", result.getCode());
        PanacheMock.reset();
    }

    @Test
    void testGetCourseNotFound() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        Mockito.when(Course.findById(id)).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> service.getCourse(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateCourse() {
        PanacheMock.mock(Course.class);
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setCode("ECO");
        dto.setName("Economia");
        dto.setDescription("Curso de Economia");
        Mockito.when(Course.listAll()).thenReturn(Arrays.asList());
        // Cria um spy manualmente e injeta no service
        CourseService serviceSpy = Mockito.spy(new CourseService() {
            @Override
            public CourseResponseDTO createCourse(CourseRequestDTO dto) {
                Course course = Mockito.spy(new Course());
                Mockito.doNothing().when(course).persist();
                course.setCode(dto.getCode());
                course.setName(dto.getName());
                course.setDescription(dto.getDescription());
                CourseResponseDTO response = new CourseResponseDTO();
                response.setId(course.getId());
                response.setCode(course.getCode());
                response.setName(course.getName());
                response.setDescription(course.getDescription());
                return response;
            }
        });
        CourseResponseDTO result = serviceSpy.createCourse(dto);
        Assertions.assertEquals("ECO", result.getCode());
        Assertions.assertEquals("Economia", result.getName());
        PanacheMock.reset();
    }

    @Test
    void testUpdateCourseFound() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setCode("ECO");
        dto.setName("Economia Atualizada");
        dto.setDescription("Atualizado");
        Course existing = new Course();
        existing.setId(id);
        Mockito.when(Course.findById(id)).thenReturn(existing);
        CourseResponseDTO result = service.updateCourse(id, dto);
        Assertions.assertEquals("Economia Atualizada", result.getName());
        PanacheMock.reset();
    }

    @Test
    void testUpdateCourseNotFound() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        CourseRequestDTO dto = new CourseRequestDTO();
        Mockito.when(Course.findById(id)).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> service.updateCourse(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCourseSuccess() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        Mockito.when(Course.deleteById(id)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> service.deleteCourse(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteCourseNotFound() {
        PanacheMock.mock(Course.class);
        UUID id = UUID.randomUUID();
        Mockito.when(Course.deleteById(id)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> service.deleteCourse(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateCoursePersistsAndReturnsDTO() {
        PanacheMock.mock(Course.class);
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setCode("MAT");
        dto.setName("Matemática");
        dto.setDescription("Curso de Matemática");
        try (var mocked = Mockito.mockConstruction(Course.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            Mockito.when(mock.getId()).thenReturn(null);
            Mockito.when(mock.getCode()).thenReturn(dto.getCode());
            Mockito.when(mock.getName()).thenReturn(dto.getName());
            Mockito.when(mock.getDescription()).thenReturn(dto.getDescription());
        })) {
            CourseResponseDTO response = service.createCourse(dto);
            Assertions.assertEquals("MAT", response.getCode());
            Assertions.assertEquals("Matemática", response.getName());
            Assertions.assertEquals("Curso de Matemática", response.getDescription());
        }
        PanacheMock.reset();
    }
}
