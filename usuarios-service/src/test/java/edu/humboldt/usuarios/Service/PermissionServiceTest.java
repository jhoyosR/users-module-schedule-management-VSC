package edu.humboldt.usuarios.Service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Repository.PermissionRepository;

/**
 * Pruebas para {@link PermissionService}.
 */
@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    /**
     * Devuelve correctamente todos los permisos.
     */
    @Test
    void getAllPermissions_ok() {
        List<Permission> list = List.of(Permission.builder().id("p").code("R").description("read").build());
        when(permissionRepository.findAll()).thenReturn(list);

        ResponseEntity<List<Permission>> response = permissionService.getAllPermissions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(list);
    }
}
