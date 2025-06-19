package edu.humboldt.usuarios.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "password_reset_tokens")
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor sin parámetros
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón Builder para crear instancias
public class PasswordResetToken {
    
    @Id
    private String id; // MongoDB usa String para _id por defecto
    
    /**
     * Token/código de verificación enviado al usuario (6 dígitos)
     * Indexado y único para búsquedas rápidas
     */
    @Indexed(unique = true)
    @NonNull
    private String token;
    
    /**
     * Email del usuario que solicitó el reset
     * Indexado para búsquedas eficientes por email
     */
    @Indexed
    @NonNull
    private String email;
    
    /**
     * Fecha y hora de expiración del token (15 minutos desde creación)
     * Indexado para limpieza automática de tokens expirados
     */
    @Indexed(expireAfterSeconds = 0) // MongoDB TTL index
    @NonNull
    private LocalDateTime expirationDate;
    
    /**
     * Indica si el token ya fue utilizado para evitar reutilización
     */
    @Builder.Default
    private boolean used = false;
    
    /**
     * Verifica si el token ha expirado comparando con la hora actual
     * @return true si el token ha expirado
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
}

