// package edu.humboldt.usuarios.User;

// import java.util.List;
// import java.util.Optional;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.beans.factory.annotation.Autowired;

// @RestController
// @RequestMapping("/api/users")
// public class UserController {
//     @Autowired private UserService userService;

//     @GetMapping
//     public List<User> getAll() { 
//         return userService.findAll(); 
//     }

//     @GetMapping("/{id}")
//     public Optional<User> getById(@PathVariable String id) {
//         return userService.findById(id);
//     }

//     @PostMapping
//     public User create(@RequestBody User user) {
//         return userService.save(user);
//     }

//     @PutMapping("/{id}")
//     public User update(@PathVariable String id, @RequestBody User user) {
//         user.setId(id);
//         return userService.save(user);
//     }

//     @DeleteMapping("/{id}")
//     public void delete(@PathVariable String id) {
//         userService.deleteById(id);
//     }
// }
