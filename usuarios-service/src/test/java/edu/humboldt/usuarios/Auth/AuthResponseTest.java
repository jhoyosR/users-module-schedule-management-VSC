package edu.humboldt.usuarios.Auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testAllArgsConstructor() {
        AuthResponse response = new AuthResponse("mi_token");
        assertEquals("mi_token", response.getToken());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        AuthResponse response = new AuthResponse();
        response.setToken("otro_token");
        assertEquals("otro_token", response.getToken());
    }

    @Test
    void testBuilder() {
        AuthResponse response = AuthResponse.builder()
                .token("builder_token")
                .build();
        assertEquals("builder_token", response.getToken());
    }

    @Test
    void testToString() {
        AuthResponse response = new AuthResponse("token123");
        assertTrue(response.toString().contains("token123"));
    }
}