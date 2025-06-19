package edu.humboldt.usuarios.Jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Pruebas unitarias sencillas sobre generación y validación de tokens.
 * No se valida la firma criptográfica (responsabilidad de la librería),
 * solo la lógica de expiración y coincidencia de username.
 */
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void tokenContainsUsername_andIsValid() {
        // Crea un objeto UserDetails simulado (mock) para evitar depender de una implementación real
        UserDetails user = mock(UserDetails.class);
        // Configura el mock para que cuando se llame a getUsername() devuelva "user"
        when(user.getUsername()).thenReturn("user");

        // Genera un token JWT utilizando el servicio y el usuario simulado
        String token = jwtService.getToken(user);

        // Comprueba que el username extraído del token coincide con el esperado
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo("user");
        // Comprueba que el token es considerado válido para el usuario proporcionado
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }
//

}
