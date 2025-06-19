package edu.humboldt.usuarios.Auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import edu.humboldt.usuarios.Jwt.JwtService;
import edu.humboldt.usuarios.Repository.UserRepository;

/**
 * Pruebas para {@link AuthAdminService} (solo login, no hay register).
 */
@ExtendWith(MockitoExtension.class)
class AuthAdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthAdminService authAdminService;

    /**
     * Login exitoso genera token.
     */
    @Test
    void login_ok() {
        // Arrange: se construye la petición de login con credenciales válidas.
        LoginRequest req = LoginRequest.builder().username("admin").password("pw").build();
        // Se crea un UserDetails simulado que representa al admin autenticable.
        UserDetails user = mock(UserDetails.class);

        // Se configura el mock del repositorio para que devuelva el usuario cuando se busque por nombre.
        // when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        // Se configura el servicio JWT para que devuelva un token fijo.
        when(jwtService.getToken(user)).thenReturn("tok");

        // Act: se ejecuta el método login del servicio.
        AuthResponse resp = authAdminService.login(req);

        // Assert: se valida que el manager de autenticación haya sido invocado con las credenciales correctas.
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("admin", "pw"));
        // Se espera que el token devuelto en la respuesta sea el que generamos en el mock.
        assertThat(resp.getToken()).isEqualTo("tok");
    }
}
