package edu.humboldt.usuarios.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para restablecer la contraseña usando el código recibido
 * Incluye validaciones de seguridad para la nueva contraseña
 */
@Data
public class ResetPasswordRequest {
    
    /**
     * Token/código de verificación recibido por email
     */
    @NotBlank(message = "El código de verificación es requerido")
    private String token;
    
    /**
     * Nueva contraseña del usuario
     * Debe tener al menos 8 caracteres para seguridad básica
     */
    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String newPassword;
    
    /**
     * Confirmación de la nueva contraseña
     * Se valida en el servicio que coincida con newPassword
     */
    @NotBlank(message = "La confirmación de contraseña es requerida")
    private String confirmPassword;
}
