package br.com.unifor.rest;

import br.com.unifor.dto.RoleRequestDTO;
import br.com.unifor.dto.RoleResponseDTO;
import br.com.unifor.service.RoleService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class RoleResourceTest {

    @Inject
    RoleResource roleResource;

    @InjectMock
    RoleService roleService;

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testListRoles() {
        RoleResponseDTO r1 = new RoleResponseDTO();
        r1.setId(UUID.randomUUID());
        r1.setName("ADMIN");
        RoleResponseDTO r2 = new RoleResponseDTO();
        r2.setId(UUID.randomUUID());
        r2.setName("COORDENADOR");
        List<RoleResponseDTO> roles = Arrays.asList(r1, r2);
        Mockito.when(roleService.listRoles()).thenReturn(roles);

        List<RoleResponseDTO> result = roleResource.list();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("ADMIN", result.get(0).getName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testGetRole() {
        UUID id = UUID.randomUUID();
        RoleResponseDTO r = new RoleResponseDTO();
        r.setId(id);
        r.setName("ADMIN");
        Mockito.when(roleService.getRole(id)).thenReturn(r);

        Response response = roleResource.get(id);
        Assertions.assertEquals(200, response.getStatus());
        RoleResponseDTO result = (RoleResponseDTO) response.getEntity();
        Assertions.assertEquals("ADMIN", result.getName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCreateRole() {
        RoleRequestDTO request = new RoleRequestDTO();
        request.setName("NEW_ROLE");
        RoleResponseDTO created = new RoleResponseDTO();
        UUID id = UUID.randomUUID();
        created.setId(id);
        created.setName("NEW_ROLE");
        Mockito.when(roleService.createRole(request)).thenReturn(created);

        Response response = roleResource.create(request);
        Assertions.assertEquals(201, response.getStatus());
        RoleResponseDTO result = (RoleResponseDTO) response.getEntity();
        Assertions.assertEquals("NEW_ROLE", result.getName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testUpdateRole() {
        UUID id = UUID.randomUUID();
        RoleRequestDTO request = new RoleRequestDTO();
        request.setName("UPDATED_ROLE");
        RoleResponseDTO updated = new RoleResponseDTO();
        updated.setId(id);
        updated.setName("UPDATED_ROLE");
        Mockito.when(roleService.updateRole(id, request)).thenReturn(updated);

        Response response = roleResource.update(id, request);
        Assertions.assertEquals(200, response.getStatus());
        RoleResponseDTO result = (RoleResponseDTO) response.getEntity();
        Assertions.assertEquals("UPDATED_ROLE", result.getName());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testDeleteRole() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(roleService).deleteRole(id);

        Response response = roleResource.delete(id);
        Assertions.assertEquals(204, response.getStatus());
    }
}

