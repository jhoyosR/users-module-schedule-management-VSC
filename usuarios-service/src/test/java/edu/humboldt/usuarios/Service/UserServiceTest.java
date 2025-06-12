package edu.humboldt.usuarios.Service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Repository.UserRepository;
import edu.humboldt.usuarios.Request.CreateUserRequest;
import edu.humboldt.usuarios.Request.UpdateUserRequest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id("roleId")
                .name("Admin")
                .description("Admin role")
                .permissions(List.of())
                .build();
    }

    /**
     * Caso feliz para getAllUsers().
     * Simula que el repositorio devuelve una lista con usuarios y se espera que el servicio
     * responda con HTTP 200 (OK) y el mismo contenido.
     */
    @Test
    void getAllUsers_shouldReturnOk() {
        List<User> users = Arrays.asList(User.builder().id("1").username("user").build());
        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userService.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyElementsOf(users);
    }

    /**
     * Busca un usuario existente por su id.
     * Se stubbea el repositorio para que lo retorne y se espera respuesta 200 (OK) y
     * el usuario en el cuerpo.
     */
    @Test
    void getUserById_found() {
        User user = User.builder().id("1").username("user").build();
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userService.getUserById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
    }

    /**
     * Intenta obtener un usuario que NO existe.
     * El repositorio devuelve Optional.empty() y se debe retornar 404 (NOT_FOUND).
     */
    @Test
    void getUserById_notFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userService.getUserById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Crea un usuario de forma exitosa.
     * 1. El servicio de roles devuelve un rol válido.
     * 2. Se codifica la contraseña.
     * 3. El repositorio guarda y devuelve el usuario con id asignado.
     * Se espera un HTTP 201 (CREATED) y que los datos en el cuerpo coincidan.
     */
    @Test
    void create_shouldReturnCreated() {
        CreateUserRequest req = CreateUserRequest.builder()
                .username("newuser")
                .email("test@cue.edu.co")
                .password("secret")
                .active(true)
                .roleId("roleId")
                .build();

        when(roleService.getRoleById("roleId")).thenReturn(ResponseEntity.ok(role));
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId("1");
            return u;
        });

        ResponseEntity<User> response = userService.create(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User saved = response.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getPassword()).isEqualTo("encoded");
    }

    /**
     * Intenta actualizar un usuario que no existe.
     * El repositorio no lo encuentra, por tanto se debe responder 404 (NOT_FOUND).
     */
    @Test
    void update_notFound() {
        UpdateUserRequest req = UpdateUserRequest.builder()
                .username("up")
                .email("up@cue.edu.co")
                .password("secret")
                .active(true)
                .roleId("roleId")
                .build();

        when(userRepository.findById("1")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userService.update("1", req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
