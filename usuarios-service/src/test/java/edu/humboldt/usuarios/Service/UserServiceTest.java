package edu.humboldt.usuarios.Service;

import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


// Estas pruebas unitarias solo se puede ejecutar con la base de datos de mongodb en ejecución
class UserServiceTest {

    private UserRepository userRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Configura el mock y el servicio antes de cada test
        userRepo = mock(UserRepository.class);
        UserService userService = new UserService();
        // Inyecta el mock manualmente porque en el test no se usa @Autowired
        userService.userRepo = userRepo;
    }

    @Test // Prueba obtener todos los usuarios
    void testFindAll() {
        when(userRepo.findAll()).thenReturn(List.of(new User()));
        assertFalse(userService.findAll().isEmpty());
    }

    @Test // Prueba buscar usuario por ID
    void testFindById() {
        User user = new User();
        user.setId("1");
        when(userRepo.findById("1")).thenReturn(Optional.of(user));
        assertTrue(userService.findById("1").isPresent());
    }

    @Test // Prueba guardar usuario
    void testSave() {
        User user = new User();
        when(userRepo.save(user)).thenReturn(user);
        assertEquals(user, userService.save(user));
    }

    @Test // Prueba eliminar usuario por ID
    void testDeleteById() {
        doNothing().when(userRepo).deleteById("1");
        userService.deleteById("1");
        verify(userRepo).deleteById("1");
    }
}