package br.com.unifor.rest;

import br.com.unifor.dto.CourseRequestDTO;
import br.com.unifor.dto.CourseResponseDTO;
import br.com.unifor.service.CourseService;
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
class CourseResourceTest {

    @Inject
    CourseResource courseResource;

    @InjectMock
    CourseService courseService;

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testListCourses() {
        CourseResponseDTO c1 = new CourseResponseDTO();
        c1.setId(UUID.randomUUID());
        c1.setCode("INF");
        c1.setName("Engenharia de Software");
        c1.setDescription("Curso de Engenharia de Software");
        CourseResponseDTO c2 = new CourseResponseDTO();
        c2.setId(UUID.randomUUID());
        c2.setCode("ADM");
        c2.setName("Administração");
        c2.setDescription("Curso de Administração");
        List<CourseResponseDTO> courses = Arrays.asList(c1, c2);
        Mockito.when(courseService.listCourses()).thenReturn(courses);

        List<CourseResponseDTO> result = courseResource.list();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("INF", result.get(0).getCode());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testGetCourse() {
        UUID id = UUID.randomUUID();
        CourseResponseDTO c = new CourseResponseDTO();
        c.setId(id);
        c.setCode("INF");
        c.setName("Engenharia de Software");
        c.setDescription("Curso de Engenharia de Software");
        Mockito.when(courseService.getCourse(id)).thenReturn(c);

        Response response = courseResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        CourseResponseDTO result = (CourseResponseDTO) response.getEntity();
        Assertions.assertEquals("INF", result.getCode());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testCreateCourse() {
        CourseRequestDTO request = new CourseRequestDTO();
        request.setCode("ECO");
        request.setName("Economia");
        request.setDescription("Curso de Economia");
        CourseResponseDTO created = new CourseResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        created.setCode("ECO");
        created.setName("Economia");
        created.setDescription("Curso de Economia");
        Mockito.when(courseService.createCourse(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/course/" + id);
            }
            return invocation.callRealMethod();
        }));

        Response response = courseResource.create(request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/course/" + id, response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testUpdateCourse() {
        UUID id = UUID.randomUUID();
        CourseRequestDTO request = new CourseRequestDTO();
        request.setCode("ECO");
        request.setName("Economia Atualizada");
        request.setDescription("Curso de Economia atualizado");
        CourseResponseDTO updated = new CourseResponseDTO();
        updated.setId(id);
        updated.setCode("ECO");
        updated.setName("Economia Atualizada");
        updated.setDescription("Curso de Economia atualizado");
        Mockito.when(courseService.updateCourse(id, request)).thenReturn(updated);

        Response response = courseResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        CourseResponseDTO result = (CourseResponseDTO) response.getEntity();
        Assertions.assertEquals("Economia Atualizada", result.getName());
    }

    @Test
    @TestSecurity(user = "coordenador", roles = {"COORDENADOR", "ADMIN"})
    void testDeleteCourse() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(courseService).deleteCourse(id);

        Response response = courseResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}

