package edu.humboldt.usuarios.Controller;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Request.CreateUserRequest;
import edu.humboldt.usuarios.Request.UpdateUserRequest;
import edu.humboldt.usuarios.Service.RoleService;
import edu.humboldt.usuarios.Service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('list_user')")
    @GetMapping
    public List<User> getAll() { 
        return userService.findAll(); 
    }

    @PreAuthorize("hasAuthority('list_user')")
    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('create_user')")
    @PostMapping
    public User create(@RequestBody CreateUserRequest request) {
        Optional<Role> optionalRole = roleService.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            return null;
        }
        Role role = optionalRole.get();

        User user = User.builder()
        .id(null)
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .active(request.isActive())
        .role(role)
        .build();
        
        return userService.save(user);
    }

    @PreAuthorize("hasAuthority('edit_user')")
    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        Optional<Role> optionalRole = roleService.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        Role role = optionalRole.get();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(request.isActive());
        user.setRole(role);

        return userService.save(user);
    }

    @PreAuthorize("hasAuthority('delete_user')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }
}
