package edu.humboldt.usuarios.Entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test // Prueba constructores y getters/setters
    void testRoleProperties() {
        Role role = new Role();
        role.setId("1");
        role.setName("ADMIN");

        assertEquals("1", role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test // Prueba el builder
    void testRoleBuilder() {
        Role role = Role.builder()
                .id("2")
                .name("USER")
                .build();

        assertEquals("2", role.getId());
        assertEquals("USER", role.getName());
    }

    @Test // Prueba equals y hashCode
    void testEqualsAndHashCode() {
        Role role1 = Role.builder().id("1").name("A").build();
        Role role2 = Role.builder().id("1").name("A").build();
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test // Prueba toString
    void testToString() {
        Role role = Role.builder().name("test").build();
        assertTrue(role.toString().contains("test"));
    }
}