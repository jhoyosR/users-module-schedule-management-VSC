package edu.humboldt.usuarios.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import edu.humboldt.usuarios.Repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            return true;
        }
        return !userRepository.existsByUsername(username);
    }
}

