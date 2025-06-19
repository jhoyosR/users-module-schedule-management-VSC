package edu.humboldt.usuarios.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import edu.humboldt.usuarios.Request.ForgotPasswordRequest;
import edu.humboldt.usuarios.Request.ResetPasswordRequest;
import edu.humboldt.usuarios.Service.PasswordResetService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para endpoints de recuperación de contraseña
 * Expone endpoints públicos para solicitar y confirmar reset de contraseña
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PasswordResetController {
    
    private final PasswordResetService passwordResetService;
    
    /**
     * Endpoint para solicitar recuperación de contraseña
     * Envía un código de verificación al email especificado
     * 
     * @param request Objeto con el email del usuario
     * @return Respuesta indicando que se procesó la solicitud
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        
        try {
            log.info("Solicitud de recuperación de contraseña recibida");
            
            passwordResetService.initiatePasswordReset(request.getEmail());
            
            // Respuesta genérica por seguridad (no revelar si el email existe)
            Map<String, String> response = new HashMap<>();
            response.put("message", "Si el email está registrado, recibirás un código de verificación");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error en solicitud de recuperación: {}", e.getMessage());
            
            // Por seguridad, no revelar detalles específicos del error
            Map<String, String> response = new HashMap<>();
            response.put("message", "Si el email está registrado, recibirás un código de verificación");
            response.put("status", "processed");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error interno en forgot-password: {}", e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error interno del servidor. Inténtalo más tarde.");
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para restablecer contraseña usando código de verificación
     * Valida el código y actualiza la contraseña del usuario
     * 
     * @param request Objeto con token, nueva contraseña y confirmación
     * @return Respuesta indicando el resultado de la operación
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        
        try {
            log.info("Solicitud de reset de contraseña recibida");
            
            passwordResetService.resetPassword(request);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Contraseña actualizada exitosamente");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.warn("Error de validación en reset-password: {}", e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "validation_error");
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            log.error("Error interno en reset-password: {}", e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error interno del servidor. Inténtalo más tarde.");
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

