package edu.humboldt.usuarios.Auth;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testAllArgsConstructor() {
        LoginRequest req = new LoginRequest("usuario", "clave");
        assertEquals("usuario", req.getUsername());
        assertEquals("clave", req.getPassword());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LoginRequest req = new LoginRequest();
        req.setUsername("otro");
        req.setPassword("1234");

        assertEquals("otro", req.getUsername());
        assertEquals("1234", req.getPassword());
    }

    @Test
    void testBuilder() {
        LoginRequest req = LoginRequest.builder()
                .username("builder")
                .password("builderpass")
                .build();

        assertEquals("builder", req.getUsername());
        assertEquals("builderpass", req.getPassword());
    }

    @Test
    void testToString() {
        LoginRequest req = new LoginRequest("u", "p");
        assertTrue(req.toString().contains("u"));
        assertTrue(req.toString().contains("p"));
    }
} 
