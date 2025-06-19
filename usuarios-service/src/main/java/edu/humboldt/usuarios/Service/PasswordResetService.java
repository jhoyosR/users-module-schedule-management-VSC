package edu.humboldt.usuarios.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.humboldt.usuarios.Entities.PasswordResetToken;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Repository.PasswordResetTokenRepository;
import edu.humboldt.usuarios.Repository.UserRepository;
import edu.humboldt.usuarios.Request.ResetPasswordRequest;

import org.springframework.scheduling.annotation.Scheduled;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Servicio principal para la funcionalidad de recuperación de contraseña
 * Maneja la lógica de negocio para generar tokens, validarlos y actualizar contraseñas
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // Asegura consistencia en operaciones de base de datos
public class PasswordResetService {
    
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    // Generador seguro de números aleatorios para códigos
    private final SecureRandom random = new SecureRandom();
    
    // Duración del token en minutos
    private static final int TOKEN_EXPIRATION_MINUTES = 15;
    
    /**
     * Inicia el proceso de recuperación de contraseña
     * 1. Verifica que el usuario existe
     * 2. Invalida tokens anteriores del mismo email
     * 3. Genera nuevo código de verificación
     * 4. Guarda el token en base de datos
     * 5. Envía email con el código
     * 
     * @param email Email del usuario que solicita recuperación
     * @throws RuntimeException si el usuario no existe
     */
    public void initiatePasswordReset(String email) {
        log.info("Iniciando recuperación de contraseña para: {}", email);
        
        // Verificar que el usuario existe en la base de datos
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Intento de recuperación para email no registrado: {}", email);
                return new RuntimeException("Usuario no encontrado");
            });
        
        // Invalidar cualquier token anterior del mismo email para seguridad
        tokenRepository.findByEmailAndUsedFalse(email)
            .ifPresent(existingToken -> {
                log.info("Invalidando token anterior para: {}", email);
                existingToken.setUsed(true);
                tokenRepository.save(existingToken);
            });
        
        // Generar nuevo código de verificación de 6 dígitos
        String code = generateResetCode();
        
        // Crear nuevo token con expiración
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(code)
                .email(email)
                .expirationDate(LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES))
                .used(false)
                .build();
        
        // Guardar token en MongoDB
        tokenRepository.save(resetToken);
        log.info("Token de recuperación generado para: {}", email);
        
        // Enviar email con el código
        emailService.sendPasswordResetCode(email, code);
    }
    
    /**
     * Restablece la contraseña del usuario usando el código de verificación
     * 1. Valida que las contraseñas coinciden
     * 2. Busca y valida el token
     * 3. Verifica que no ha expirado
     * 4. Busca el usuario asociado
     * 5. Actualiza la contraseña encriptada
     * 6. Marca el token como usado
     * 
     * @param request Objeto con token, nueva contraseña y confirmación
     * @throws RuntimeException si hay algún error en la validación
     */
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Procesando reset de contraseña con token: {}", request.getToken());
        
        // Validar que las contraseñas coinciden
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.warn("Las contraseñas no coinciden para token: {}", request.getToken());
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        
        // Buscar token válido y no usado
        PasswordResetToken resetToken = tokenRepository.findByTokenAndUsedFalse(request.getToken())
            .orElseThrow(() -> {
                log.warn("Token inválido o ya usado: {}", request.getToken());
                return new RuntimeException("Código de verificación inválido o ya utilizado");
            });
        
        // Verificar que el token no ha expirado
        if (resetToken.isExpired()) {
            log.warn("Token expirado: {} para email: {}", request.getToken(), resetToken.getEmail());
            throw new RuntimeException("El código de verificación ha expirado. Solicita uno nuevo.");
        }
        
        // Buscar usuario asociado al token
        User user = userRepository.findByEmail(resetToken.getEmail())
            .orElseThrow(() -> {
                log.error("Usuario no encontrado para email: {}", resetToken.getEmail());
                return new RuntimeException("Usuario no encontrado");
            });
        
        // Encriptar y actualizar la nueva contraseña
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        
        // Marcar token como usado para evitar reutilización
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        log.info("Contraseña actualizada exitosamente para: {}", resetToken.getEmail());
    }
    
    /**
     * Genera un código de verificación de 6 dígitos usando SecureRandom
     * @return Código numérico de 6 dígitos como String
     */
    private String generateResetCode() {
        // Genera número entre 0 y 999999, formatea con ceros a la izquierda
        return String.format("%06d", random.nextInt(1000000));
    }
    
    /**
     * Tarea programada que se ejecuta cada hora para limpiar tokens expirados
     * MongoDB TTL se encarga automáticamente, pero esto es un respaldo
     */
    @Scheduled(fixedRate = 3600000) // 3600000 ms = 1 hora
    public void cleanupExpiredTokens() {
        log.debug("Ejecutando limpieza de tokens expirados...");
        LocalDateTime cutoffTime = LocalDateTime.now();
        
        try {
            tokenRepository.deleteByExpirationDateBefore(cutoffTime);
            log.debug("Limpieza de tokens completada");
        } catch (Exception e) {
            log.error("Error en limpieza de tokens: {}", e.getMessage());
        }
    }
}

