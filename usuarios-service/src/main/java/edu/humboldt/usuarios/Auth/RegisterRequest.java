package edu.humboldt.usuarios.Auth;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 5, max = 25, message = "El nombre de usuario debe tener entre 5 y 25 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    @Pattern(
        regexp = "^[\\w.%+-]+@(cue\\.edu\\.co|unihumboldt\\.edu\\.co)$",
        message = "El correo debe pertenecer a los dominios cue.edu.co o unihumboldt.edu.co"
    )
    private String email;
}
