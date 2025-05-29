package edu.humboldt.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.humboldt.usuarios.Entities.Permission;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByCode(String code);
}
