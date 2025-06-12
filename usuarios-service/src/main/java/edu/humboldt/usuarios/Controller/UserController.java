package edu.humboldt.usuarios.Controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Request.CreateUserRequest;
import edu.humboldt.usuarios.Request.UpdateUserRequest;
import edu.humboldt.usuarios.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('list_user')")
    @GetMapping
    public ResponseEntity<List<User>> getAll() { 
        return userService.getAllUsers(); 
    }

    @PreAuthorize("hasAuthority('list_user')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAuthority('create_user')")
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PreAuthorize("hasAuthority('edit_user')")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable String id,@Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @PreAuthorize("hasAuthority('delete_user')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return userService.deleteById(id);
    }
}
