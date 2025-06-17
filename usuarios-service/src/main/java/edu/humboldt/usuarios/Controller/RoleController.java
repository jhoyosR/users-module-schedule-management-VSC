package edu.humboldt.usuarios.Controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Request.CreateRoleRequest;
import edu.humboldt.usuarios.Request.UpdateRoleRequest;
import edu.humboldt.usuarios.Service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
@Tag(name = "Role", description = "Controller for Roles")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('list_role')")
    @GetMapping
    public ResponseEntity<List<Role>> getAll() { 
        return roleService.getAllRoles(); 
    }

    @PreAuthorize("hasAuthority('list_role')")
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable String id) {
        return roleService.getRoleById(id);
    }

    @PreAuthorize("hasAuthority('create_role')")
    @PostMapping
    public ResponseEntity<Role> create(@Valid @RequestBody CreateRoleRequest request) {
        return roleService.createRole(request);
    }

    @PreAuthorize("hasAuthority('edit_role')")
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable String id, @Valid @RequestBody UpdateRoleRequest request) {
        return roleService.updateRole(id, request);
    }

    @PreAuthorize("hasAuthority('delete_role')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return roleService.deleteRole(id);
    }
}
