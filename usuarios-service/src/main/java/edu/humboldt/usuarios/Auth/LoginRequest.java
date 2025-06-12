package edu.humboldt.usuarios.Auth;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 5, max = 25, message = "El nombre de usuario debe tener entre 5 y 25 caracteres")
    String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password; 
}
