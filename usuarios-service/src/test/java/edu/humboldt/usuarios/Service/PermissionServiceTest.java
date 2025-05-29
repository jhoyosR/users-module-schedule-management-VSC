package edu.humboldt.usuarios.Service;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    private PermissionRepository repository;
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        // Configura el mock y el servicio antes de cada test
        repository = mock(PermissionRepository.class);
        permissionService = new PermissionService(repository);
    }

    @Test // Prueba obtener todos los permisos
    void testGetAllPermissions() {
        when(repository.findAll()).thenReturn(List.of(new Permission()));
        assertFalse(permissionService.getAllPermissions().isEmpty());
    }

    @Test // Prueba buscar permisos por IDs
    void testFindAllById() {
        List<Permission> permissions = List.of(new Permission());
        when(repository.findAllById(List.of("1", "2"))).thenReturn(permissions);
        assertEquals(permissions, permissionService.findAllById(List.of("1", "2")));
    }
}