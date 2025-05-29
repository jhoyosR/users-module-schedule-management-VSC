package edu.humboldt.usuarios.Auth;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Jwt.JwtService;
import edu.humboldt.usuarios.Repository.RoleRepository;
import edu.humboldt.usuarios.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // Configura los mocks y el servicio antes de cada test
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        roleRepository = mock(RoleRepository.class);
        authService = new AuthService(userRepository, jwtService, passwordEncoder, authenticationManager, roleRepository);
    }

    @Test
    void testLogin() {
        // Prueba el login exitoso
        LoginRequest request = new LoginRequest("user", "pass");
        User user = mock(User.class);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn("token123");

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("token123", response.getToken());
    }

    @Test
    void testRegister() {
        // Prueba el registro exitoso
        RegisterRequest request = new RegisterRequest("user", "pass", "mail@mail.com");
        Role studentRole = new Role();
        studentRole.setName("Estudiante");

        when(roleRepository.findByName("Estudiante")).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(jwtService.getToken(any(User.class))).thenReturn("token456");

        AuthResponse response = authService.register(request);

        verify(userRepository).save(any(User.class));
        assertEquals("token456", response.getToken());
    }
}