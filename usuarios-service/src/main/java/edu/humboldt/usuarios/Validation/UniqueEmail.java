package edu.humboldt.usuarios.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "El correo ya está registrado";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

