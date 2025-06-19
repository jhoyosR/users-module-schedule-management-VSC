package edu.humboldt.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.humboldt.usuarios.Entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username); 
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username, String id);
    boolean existsByEmailAndIdNot(String email, String id);
    // Comprueba si existe algún usuario cuyo role.id sea el dado
    boolean existsByRole_Id(String roleId);
}
