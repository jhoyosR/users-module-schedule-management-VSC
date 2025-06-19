package edu.humboldt.usuarios.Request;

import lombok.Data;
import jakarta.validation.constraints.*;

/**
 * DTO para la solicitud de recuperación de contraseña
 * Contiene solo el email del usuario
 */
@Data
public class ForgotPasswordRequest {
    
    /**
     * Email del usuario que solicita recuperar la contraseña
     * Debe ser un email válido y no estar vacío
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    @Pattern(
        regexp = "^[\\w.%+-]+@(cue\\.edu\\.co|unihumboldt\\.edu\\.co)$",
        message = "El correo debe pertenecer a los dominios cue.edu.co o unihumboldt.edu.co"
    )
    private String email;
}
