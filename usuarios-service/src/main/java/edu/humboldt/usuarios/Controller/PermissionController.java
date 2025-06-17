package edu.humboldt.usuarios.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
@Tag(name = "Permission", description = "Controller for Permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasAuthority('list_permission')")
    @GetMapping
    public ResponseEntity<List<Permission>> getAll() {
        return permissionService.getAllPermissions();
    }
}
