package edu.humboldt.usuarios.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Repository.PermissionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return ResponseEntity.ok(permissions);
    }

    public List<Permission> findAllById(Iterable<String> ids) {
        return permissionRepository.findAllById(ids);
    }
}
