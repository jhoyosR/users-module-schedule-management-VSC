package edu.humboldt.usuarios.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Exception.DuplicateResourceException;
import edu.humboldt.usuarios.Repository.RoleRepository;
import edu.humboldt.usuarios.Request.CreateRoleRequest;
import edu.humboldt.usuarios.Request.UpdateRoleRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    public ResponseEntity<Role> getRoleById(String id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            return ResponseEntity.ok(optionalRole.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Role> createRole(CreateRoleRequest request) {
        List<Permission> permissions = permissionService.findAllById(request.getPermissionIds());

        Role role = new Role(null, request.getName(), request.getDescription(), permissions);
        Role saved = roleRepository.save(role);
        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<Role> updateRole(String id, UpdateRoleRequest request) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (roleRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("El nombre del rol ya está en uso");
        }

        Role role = optionalRole.get();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(permissionService.findAllById(request.getPermissionIds()));

        Role updated = roleRepository.save(role);
        return ResponseEntity.ok(updated);
    }

    public ResponseEntity<Void> deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}