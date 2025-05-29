package edu.humboldt.usuarios.Auth;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testAllArgsConstructor() {
        RegisterRequest req = new RegisterRequest("usuario", "clave", "correo@ejemplo.com");
        assertEquals("usuario", req.getUsername());
        assertEquals("clave", req.getPassword());
        assertEquals("correo@ejemplo.com", req.getEmail());
    } // Verificación de que el constructor con todos los argumentos funciona correctamente

    @Test
    void testNoArgsConstructorAndSetters() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("otro");
        req.setPassword("1234");
        req.setEmail("otro@correo.com");

        assertEquals("otro", req.getUsername());
        assertEquals("1234", req.getPassword());
        assertEquals("otro@correo.com", req.getEmail());
    } // Verificación de que el constructor sin argumentos y los setters funcionan correctamente

    @Test
    void testBuilder() {
        RegisterRequest req = RegisterRequest.builder()
                .username("builder")
                .password("builderpass")
                .email("builder@correo.com")
                .build();

        assertEquals("builder", req.getUsername());
        assertEquals("builderpass", req.getPassword());
        assertEquals("builder@correo.com", req.getEmail());
    } // Verificación de que el patrón Builder funciona correctamente

    @Test
    void testToString() {
        RegisterRequest req = new RegisterRequest("u", "p", "e@c.com");
        assertTrue(req.toString().contains("u"));
        assertTrue(req.toString().contains("p"));
        assertTrue(req.toString().contains("e@c.com"));
    } // Verificación de que el método toString devuelve una representación adecuada del objeto
}