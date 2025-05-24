package edu.humboldt.usuarios.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }

    public List<Permission> getAllPermissions() {
        return repository.findAll();
    }
}
