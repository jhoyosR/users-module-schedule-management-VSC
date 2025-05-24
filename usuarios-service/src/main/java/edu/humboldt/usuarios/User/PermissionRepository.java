package edu.humboldt.usuarios.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByCode(String code);
}
