package edu.humboldt.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.humboldt.usuarios.Entities.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
