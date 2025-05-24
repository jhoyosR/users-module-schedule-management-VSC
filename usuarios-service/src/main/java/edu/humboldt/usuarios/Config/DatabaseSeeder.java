package edu.humboldt.usuarios.Config;

import edu.humboldt.usuarios.Entities.Permission;
import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Repository.PermissionRepository;
import edu.humboldt.usuarios.Repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public DatabaseSeeder(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository= roleRepository;
    }

    @Override
    public void run(String... args) {
        seedPermissions();
        seedRoles();
    }

    private void seedPermissions() {
        // Lista de permisos del sistema
        List<Permission> permissions = List.of(
                new Permission(null, "list_user"       , "Permite listar usuarios"),
                new Permission(null, "create_user"     , "Permite crear usuarios"),
                new Permission(null, "edit_user"       , "Permite editar usuarios"),
                new Permission(null, "delete_user"     , "Permite eliminar usuarios"),
                new Permission(null, "list_role"       , "Permite listar roles"),
                new Permission(null, "create_role"     , "Permite crear roles"),
                new Permission(null, "edit_role"       , "Permite editar roles"),
                new Permission(null, "delete_role"     , "Permite eliminar roles"),
                new Permission(null, "watch_schedule"  , "Permite ver el horario")
        );

        permissions.forEach(p -> {
            permissionRepository.findByCode(p.getCode())
                    .orElseGet(() -> permissionRepository.save(p));
        });
    }

    private void seedRoles() {

        // Buscar permisos por código
        Permission listUser        = permissionRepository.findByCode("list_user").orElseThrow();
        Permission createUser      = permissionRepository.findByCode("create_user").orElseThrow();
        Permission editUser        = permissionRepository.findByCode("edit_user").orElseThrow();
        Permission deleteUser      = permissionRepository.findByCode("delete_user").orElseThrow();
        Permission listRole        = permissionRepository.findByCode("list_role").orElseThrow();
        Permission createRole      = permissionRepository.findByCode("create_role").orElseThrow();
        Permission editRole        = permissionRepository.findByCode("edit_role").orElseThrow();
        Permission deleteRole      = permissionRepository.findByCode("delete_role").orElseThrow();
        Permission watchSchedule   = permissionRepository.findByCode("watch_schedule").orElseThrow();

        List<Role> roles = List.of(
                // Admin
                new Role(null, "Administrador", "Administrador general del sistema con todos los permisos", List.of(
                    listUser, createUser, editUser, deleteUser,
                    listRole, createRole, editRole, deleteRole,
                    watchSchedule
                )),
                // Estudiante
                new Role(null, "Estudiante", "Estudiante consulta su horario", List.of(
                watchSchedule
                )),
                // Docente
                new Role(null, "Docente", "Docente consulta su horario", List.of(
                watchSchedule
                ))
        );

        roles.forEach(r -> {
            roleRepository.findByName(r.getName())
                    .orElseGet(() -> roleRepository.save(r));
        });
    }
}