package edu.humboldt.usuarios.Service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Repository.RoleRepository;
import edu.humboldt.usuarios.Request.CreateRoleRequest;
import edu.humboldt.usuarios.Request.UpdateRoleRequest;

/**
 * Pruebas unitarias para {@link RoleService}.
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionService permissionService;
    @Mock
    private edu.humboldt.usuarios.Repository.UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    private List<Permission> permissions;
    private Role role;

    @BeforeEach
    void setUp() {
        Permission p1 = Permission.builder().id("p1").code("READ").description("read").build();
        Permission p2 = Permission.builder().id("p2").code("WRITE").description("write").build();
        permissions = List.of(p1, p2);

        role = Role.builder()
                .id("role1")
                .name("Admin")
                .description("Administrador")
                .permissions(permissions)
                .build();
    }

    /**
     * Caso feliz de obtener todos los roles.
     * Se stubbea el repositorio para devolver una lista y se espera HTTP 200.
     */
    @Test
    void getAllRoles_shouldReturnOk() {
        when(roleRepository.findAll()).thenReturn(List.of(role));

        ResponseEntity<List<Role>> response = roleService.getAllRoles();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(role);
    }

    /**
     * Obtiene un rol existente por ID.
     */
    @Test
    void getRoleById_found() {
        when(roleRepository.findById("role1")).thenReturn(Optional.of(role));

        ResponseEntity<Role> response = roleService.getRoleById("role1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(role);
    }

    /**
     * Intenta obtener un rol inexistente.
     */
    @Test
    void getRoleById_notFound() {
        when(roleRepository.findById("role1")).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleService.getRoleById("role1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Crea un rol exitosamente.
     */
    @Test
    void createRole_shouldReturnOk() {
        CreateRoleRequest req = CreateRoleRequest.builder()
                .name("Admin")
                .description("Administrador")
                .permissionIds(Arrays.asList("p1", "p2"))
                .build();

        when(permissionService.findAllById(req.getPermissionIds())).thenReturn(permissions);
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Role> response = roleService.createRole(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPermissions()).containsExactlyElementsOf(permissions);
    }

    /**
     * Intenta actualizar un rol inexistente -> 404.
     */
    @Test
    void updateRole_notFound() {
        UpdateRoleRequest req = UpdateRoleRequest.builder()
                .name("New")
                .description("Nueva desc")
                .permissionIds(List.of("p1"))
                .build();

        when(roleRepository.findById("role1")).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleService.updateRole("role1", req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Elimina un rol que no existe -> 404.
     */
    @Test
    void deleteRole_notFound() {
        when(roleRepository.existsById("role1")).thenReturn(false);

        ResponseEntity<Void> response = roleService.deleteRole("role1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Elimina un rol existente correctamente.
     */
    @Test
    void deleteRole_ok() {
        when(roleRepository.existsById("role1")).thenReturn(true);
        when(userRepository.existsByRole_Id("role1")).thenReturn(false);

        ResponseEntity<Void> response = roleService.deleteRole("role1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(roleRepository).deleteById("role1");
        verify(userRepository).existsByRole_Id("role1");
    }
}
