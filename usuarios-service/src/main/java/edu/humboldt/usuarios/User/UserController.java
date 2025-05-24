package edu.humboldt.usuarios.User;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired 
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public List<User> getAll() { 
        return userService.findAll(); 
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PostMapping
    public User create(@RequestBody CreateUserRequest request) {
        Optional<Role> optionalRole = roleService.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            return null;
        }
        Role role = optionalRole.get();

        User user = new User(null, request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), role);
        return userService.save(user);
    }

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
        user.setRole(role);

        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }
}
