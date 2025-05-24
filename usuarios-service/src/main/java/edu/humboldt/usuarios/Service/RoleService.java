package edu.humboldt.usuarios.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.humboldt.usuarios.Entities.Role;
import edu.humboldt.usuarios.Repository.RoleRepository;

@Service
public class RoleService {
    @Autowired private RoleRepository roleRepo;

    public List<Role> findAll() {
        return roleRepo.findAll();
    }
    public Optional<Role> findById(String id) {
        return roleRepo.findById(id);
    }
    public Optional<Role> findByName(String name) {
        return roleRepo.findByName(name);
    }
    public Role save(Role role) {
        return roleRepo.save(role);
    }
    public void deleteById(String id) {
        roleRepo.deleteById(id);
    }
}