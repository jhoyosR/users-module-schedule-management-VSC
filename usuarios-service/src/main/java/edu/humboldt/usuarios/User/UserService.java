package edu.humboldt.usuarios.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }
    public Optional<User> findById(String id) {
        return userRepo.findById(id);
    }
    public User save(User user) {
        return userRepo.save(user);
    }
    public void deleteById(String id) {
        userRepo.deleteById(id);
    }
}
