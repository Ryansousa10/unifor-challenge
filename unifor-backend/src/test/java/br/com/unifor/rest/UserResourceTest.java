package br.com.unifor.rest;

import br.com.unifor.dto.UserRequestDTO;
import br.com.unifor.dto.UserResponseDTO;
import br.com.unifor.service.UserService;
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
class UserResourceTest {

    @Inject
    UserResource userResource;

    @InjectMock
    UserService userService;

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testListUsers() {
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setId(UUID.randomUUID());
        user1.setFirstName("User 1");
        UserResponseDTO user2 = new UserResponseDTO();
        user2.setId(UUID.randomUUID());
        user2.setFirstName("User 2");
        List<UserResponseDTO> users = Arrays.asList(user1, user2);
        Mockito.when(userService.listUsers()).thenReturn(users);

        Response response = userResource.list();
        Assertions.assertEquals(200, response.getStatus());
        List<UserResponseDTO> result = (List<UserResponseDTO>) response.getEntity();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("User 1", result.get(0).getFirstName());
        Assertions.assertEquals("User 2", result.get(1).getFirstName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testGetUser() {
        UUID id = UUID.randomUUID();
        UserResponseDTO user = new UserResponseDTO();
        user.setId(id);
        user.setFirstName("Test User");
        Mockito.when(userService.getUser(id)).thenReturn(user);

        Response response = userResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        UserResponseDTO result = (UserResponseDTO) response.getEntity();
        Assertions.assertEquals("Test User", result.getFirstName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCreateUser() {
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("testuser");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("test@user.com");
        request.setPassword("123456");
        UserResponseDTO created = new UserResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        Mockito.when(userService.createUser(request)).thenReturn(created);

        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(Mockito.mock(UriBuilder.class, invocation -> {
            if (invocation.getMethod().getName().equals("path")) {
                return invocation.getMock();
            }
            if (invocation.getMethod().getName().equals("build")) {
                return java.net.URI.create("/users/" + id);
            }
            return invocation.callRealMethod();
        }));

        Response response = userResource.create(request, uriInfo);
        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertEquals("/users/" + id, response.getLocation().toString());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testUpdateUser() {
        UUID id = UUID.randomUUID();
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("updateduser");
        request.setFirstName("Updated");
        request.setLastName("User");
        request.setEmail("updated@user.com");
        request.setPassword("654321");
        UserResponseDTO updated = new UserResponseDTO();
        updated.setId(id);
        updated.setFirstName("Updated");
        Mockito.when(userService.updateUser(id, request)).thenReturn(updated);

        Response response = userResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        UserResponseDTO result = (UserResponseDTO) response.getEntity();
        Assertions.assertEquals("Updated", result.getFirstName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testDeleteUser() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(userService).deleteUser(id);

        Response response = userResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}
