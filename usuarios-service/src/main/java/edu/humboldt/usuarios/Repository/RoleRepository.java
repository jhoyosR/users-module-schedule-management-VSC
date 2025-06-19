package edu.humboldt.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.humboldt.usuarios.Entities.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String email, String id);
}
