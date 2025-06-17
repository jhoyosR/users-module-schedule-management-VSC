package edu.humboldt.usuarios.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import edu.humboldt.usuarios.Repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {
    
    private final RoleRepository roleRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            return true; 
        }
        return !roleRepository.existsByName(name);
    }
}
