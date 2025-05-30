package edu.humboldt.usuarios.Controller;


import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Request.CreateRoleRequest;
import edu.humboldt.usuarios.Request.UpdateRoleRequest;
import edu.humboldt.usuarios.Service.PermissionService;
import edu.humboldt.usuarios.Service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @PreAuthorize("hasAuthority('list_role')")
    @GetMapping
    public List<Role> getAll() { 
        return roleService.findAll(); 
    }

    @PreAuthorize("hasAuthority('list_role')")
    @GetMapping("/{id}")
    public Optional<Role> getById(@PathVariable String id) {
        return roleService.findById(id);
    }

    @PreAuthorize("hasAuthority('create_role')")
    @PostMapping
    public Role create(@Valid @RequestBody CreateRoleRequest request) {
        List<Permission> permissions = permissionService.findAllById(request.getPermissionIds());
        
        Role role = new Role(null, request.getName(), request.getDescription(), permissions);
        
        return roleService.save(role);
    }

    @PreAuthorize("hasAuthority('edit_role')")
    @PutMapping("/{id}")
    public Role update(@PathVariable String id, @Valid @RequestBody UpdateRoleRequest request) {
        Optional<Role> optionalRole = roleService.findById(id);
        if (optionalRole.isEmpty()) {
            return null;
        }

        Role role = optionalRole.get();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        List<Permission> permissions = permissionService.findAllById(request.getPermissionIds());
        role.setPermissions(permissions);

        
        return roleService.save(role);
    }

    @PreAuthorize("hasAuthority('delete_role')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        roleService.deleteById(id);
    }
}
