package edu.humboldt.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.humboldt.usuarios.Entities.User;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username); 
}
