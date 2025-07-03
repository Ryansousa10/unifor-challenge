package br.com.unifor.service;

import br.com.unifor.domain.Role;
import br.com.unifor.dto.RoleRequestDTO;
import br.com.unifor.dto.RoleResponseDTO;
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
class RoleServiceTest {

    RoleService service = new RoleService();

    @Test
    void testListRoles() {
        PanacheMock.mock(Role.class);

        Role r1 = new Role();
        r1.setId(UUID.randomUUID());
        r1.setName("ADMIN");

        List<Role> list = Arrays.asList(r1);
        Mockito.when(Role.listAll()).thenReturn((List) list);

        List<RoleResponseDTO> result = service.listRoles();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(r1.getId(), result.get(0).getId());
        Assertions.assertEquals(r1.getName(), result.get(0).getName());

        PanacheMock.reset();
    }

    @Test
    void testGetRoleFound() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        Role role = new Role();
        role.setId(id);
        role.setName("ADMIN");

        Mockito.when(Role.findById(id)).thenReturn(role);

        RoleResponseDTO result = service.getRole(id);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(role.getName(), result.getName());

        PanacheMock.reset();
    }

    @Test
    void testGetRoleNotFound() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Role.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.getRole(id));
        PanacheMock.reset();
    }

    @Test
    void testCreateRoleSuccess() {
        PanacheMock.mock(Role.class);

        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setName("ADMIN");

        try (var mocked = Mockito.mockConstruction(Role.class, (mock, context) -> {
            Mockito.doNothing().when(mock).persist();
            UUID id = UUID.randomUUID();
            Mockito.when(mock.getId()).thenReturn(id);
            Mockito.when(mock.getName()).thenReturn(dto.getName());
        })) {
            RoleResponseDTO response = service.createRole(dto);
            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(dto.getName(), response.getName());
        }

        PanacheMock.reset();
    }

    @Test
    void testUpdateRoleSuccess() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        Role existing = new Role();
        existing.setId(id);
        existing.setName("USER");

        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setName("ADMIN");

        Mockito.when(Role.findById(id)).thenReturn(existing);

        RoleResponseDTO response = service.updateRole(id, dto);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals(dto.getName(), response.getName());

        PanacheMock.reset();
    }

    @Test
    void testUpdateRoleNotFound() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setName("ADMIN");

        Mockito.when(Role.findById(id)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> service.updateRole(id, dto));
        PanacheMock.reset();
    }

    @Test
    void testDeleteRoleSuccess() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Role.deleteById(id)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.deleteRole(id));
        PanacheMock.reset();
    }

    @Test
    void testDeleteRoleNotFound() {
        PanacheMock.mock(Role.class);
        UUID id = UUID.randomUUID();

        Mockito.when(Role.deleteById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> service.deleteRole(id));
        PanacheMock.reset();
    }
}
