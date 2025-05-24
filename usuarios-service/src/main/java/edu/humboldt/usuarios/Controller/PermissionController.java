package edu.humboldt.usuarios.Controller;

import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
}
