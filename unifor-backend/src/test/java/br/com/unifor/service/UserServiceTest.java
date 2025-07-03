package br.com.unifor.service;

import br.com.unifor.domain.Role;
import br.com.unifor.domain.User;
import br.com.unifor.dto.UserRequestDTO;
import br.com.unifor.dto.UserResponseDTO;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.*;

@QuarkusTest
class UserServiceTest {

    UserService service = new UserService();

    @Test
    void testListUsers() {
        PanacheMock.mock(User.class);

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");

        User u1 = new User();
        u1.setId(UUID.randomUUID());
        u1.setUsername("admin");
        u1.setPassword("123456");
        u1.setFirstName("Admin");
        u1.setLastName("User");
        u1.setEmail("admin@example.com");
        u1.setCreatedAt(Instant.now());
        u1.setRoles(Set.of(role));

        List<User> list = Arrays.asList(u1);
        Mockito.when(User.listAll()).thenReturn((List) list);

        List<UserResponseDTO> result = service.listUsers();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(u1.getId(), result.get(0).getId());
        Assertions.assertEquals(u1.getUsername(), result.get(0).getUsername());
        Assertions.assertEquals(u1.getFirstName(), result.get(0).getFirstName());
        Assertions.assertEquals(u1.getLastName(), result.get(0).getLastName());
        Assertions.assertEquals(u1.getEmail(), result.get(0).getEmail());
        Assertions.assertEquals(u1.getCreatedAt(), result.get(0).getCreatedAt());
        Assertions.assertTrue(result.get(0).getRoles().contains(role.getName()));

        PanacheMock.reset();
    }

    @Test
    void testGetUserFound() {
        PanacheMock.mock(User.class);
        UUID id = UUID.randomUUID();

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");

        User user = new User();
        user.setId(id);
        user.setUsername("admin");
        user.setPassword("123456");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setEmail("admin@example.com");
        user.setCreatedAt(Instant.now());
        user.setRoles(Set.of(role));

        Mockito.when(User.findByIdOptional(id)).thenReturn(Optional.of(user));

        UserResponseDTO result = service.getUser(id);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(user.getUsername(), result.getUsername());
        Assertions.assertEquals(user.getFirstName(), result.getFirstName());
        Assertions.assertEquals(user.getLastName(), result.getLastName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getCreatedAt(), result.getCreatedAt());
        Assertions.assertTrue(result.getRoles().contains(role.getName()));

        PanacheMock.reset();
    }

    @Test
    void testGetUserNotFound() {
        PanacheMock.mock(User.class);
        UUID id = UUID.randomUUID();

        Mockito.when(User.findByIdOptional(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.getUser(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateUserSuccess() {
        PanacheMock.mock(User.class);
        PanacheMock.mock(Role.class);

        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");
        dto.setFirstName("Admin");
        dto.setLastName("User");
        dto.setEmail("admin@example.com");
        dto.setRoles(Set.of(roleId));

        // Mock da busca da role
        Mockito.when(Role.findByIdOptional(roleId)).thenReturn(Optional.of(role));

        // Mock do builder do User
        User.UserBuilder builder = Mockito.mock(User.UserBuilder.class);
        User mockUser = Mockito.mock(User.class);
        UUID id = UUID.randomUUID();
        Mockito.when(mockUser.getId()).thenReturn(id);
        Mockito.when(mockUser.getUsername()).thenReturn(dto.getUsername());
        Mockito.when(mockUser.getFirstName()).thenReturn(dto.getFirstName());
        Mockito.when(mockUser.getLastName()).thenReturn(dto.getLastName());
        Mockito.when(mockUser.getEmail()).thenReturn(dto.getEmail());
        Mockito.when(mockUser.getCreatedAt()).thenReturn(Instant.now());
        Mockito.when(mockUser.getRoles()).thenReturn(Set.of(role));
        Mockito.doNothing().when(mockUser).persist();

        Mockito.when(User.builder()).thenReturn(builder);
        Mockito.when(builder.username(dto.getUsername())).thenReturn(builder);
        Mockito.when(builder.password(dto.getPassword())).thenReturn(builder);
        Mockito.when(builder.firstName(dto.getFirstName())).thenReturn(builder);
        Mockito.when(builder.lastName(dto.getLastName())).thenReturn(builder);
        Mockito.when(builder.email(dto.getEmail())).thenReturn(builder);
        Mockito.when(builder.roles(Mockito.any())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(mockUser);

        // Executa o método e verifica o resultado
        UserResponseDTO response = service.createUser(dto);

        // Verifica se os dados foram corretamente setados
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(dto.getUsername(), response.getUsername());
        Assertions.assertEquals(dto.getFirstName(), response.getFirstName());
        Assertions.assertEquals(dto.getLastName(), response.getLastName());
        Assertions.assertEquals(dto.getEmail(), response.getEmail());
        Assertions.assertTrue(response.getRoles().contains(role.getName()));

        PanacheMock.reset();
    }

    @Test
    void testCreateUserRoleNotFound() {
        PanacheMock.mock(Role.class);

        UUID roleId = UUID.randomUUID();
        UserRequestDTO dto = new UserRequestDTO();
        dto.setRoles(Set.of(roleId));

        Mockito.when(Role.findByIdOptional(roleId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.createUser(dto));
        PanacheMock.reset();
    }

    @Test
    void testUpdateUserSuccess() {
        PanacheMock.mock(User.class);
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User existing = new User();
        existing.setId(id);
        existing.setUsername("old_user");

        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("new_user");
        dto.setPassword("new_pass");
        dto.setFirstName("New");
        dto.setLastName("User");
        dto.setEmail("new@example.com");
        dto.setRoles(Set.of(roleId));

        Mockito.when(User.findByIdOptional(id)).thenReturn(Optional.of(existing));
        Mockito.when(Role.findByIdOptional(roleId)).thenReturn(Optional.of(role));

        UserResponseDTO response = service.updateUser(id, dto);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals(dto.getUsername(), response.getUsername());
        Assertions.assertEquals(dto.getFirstName(), response.getFirstName());
        Assertions.assertEquals(dto.getLastName(), response.getLastName());
        Assertions.assertEquals(dto.getEmail(), response.getEmail());
        Assertions.assertTrue(response.getRoles().contains(role.getName()));

        PanacheMock.reset();
    }

    @Test
    void testUpdateUserNotFound() {
        PanacheMock.mock(User.class);
        UUID id = UUID.randomUUID();

        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("new_user");

        Mockito.when(User.findByIdOptional(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.updateUser(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testUpdateUserRoleNotFound() {
        PanacheMock.mock(User.class);
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User existing = new User();
        existing.setId(id);

        UserRequestDTO dto = new UserRequestDTO();
        dto.setRoles(Set.of(roleId));

        Mockito.when(User.findByIdOptional(id)).thenReturn(Optional.of(existing));
        Mockito.when(Role.findByIdOptional(roleId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> service.updateUser(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteUserSuccess() {
        PanacheMock.mock(User.class);
        UUID id = UUID.randomUUID();

        Mockito.when(User.deleteById(id)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.deleteUser(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteUserNotFound() {
        PanacheMock.mock(User.class);
        UUID id = UUID.randomUUID();

        Mockito.when(User.deleteById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteUser(id));
        PanacheMock.reset();
    }
}
