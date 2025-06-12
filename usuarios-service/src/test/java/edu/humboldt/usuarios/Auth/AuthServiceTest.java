package edu.humboldt.usuarios.Auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Jwt.JwtService;
import edu.humboldt.usuarios.Repository.RoleRepository;
import edu.humboldt.usuarios.Repository.UserRepository;

/**
 * Pruebas de AuthService (login y register).
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthService authService;

    private Role studentRole;

    @BeforeEach
    void setUp() {
        studentRole = Role.builder().id("r1").name("Estudiante").description("Est").permissions(null).build();
    }

    /**
     * Login exitoso devuelve token.
     */
    @Test
    void login_ok() {
        LoginRequest req = LoginRequest.builder().username("user").password("pw").build();
        User user = mock(User.class);                 // tipo correcto

when(userRepository.findByUsername("user"))
        .thenReturn(Optional.of(user));

when(jwtService.getToken(user)).thenReturn("tok");
        AuthResponse res = authService.login(req);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("user", "pw"));
        assertThat(res.getToken()).isEqualTo("tok");
    }

    /**
     * Registro exitoso de estudiante.
     */
    @Test
    void register_ok() {
        RegisterRequest req = RegisterRequest.builder()
                .username("new")
                .password("pw")
                .email("a@cue.edu.co")
                .build();

        when(roleRepository.findByName("Estudiante")).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("pw")).thenReturn("enc");

        AuthResponse res = authService.register(req);

        verify(userRepository).save(any(User.class));
        assertThat(res.getToken()).isNotNull();
    }
}
