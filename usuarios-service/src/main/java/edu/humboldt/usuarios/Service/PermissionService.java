package edu.humboldt.usuarios.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }

    public List<Permission> getAllPermissions() {
        return repository.findAll();
    }

    public List<Permission> findAllById(Iterable<String> ids) {
        return repository.findAllById(ids);
    }
}
