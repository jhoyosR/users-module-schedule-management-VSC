package edu.humboldt.usuarios.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.humboldt.usuarios.Entities.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    
    /**
     * Busca un token activo (no usado) por su valor
     * @param token Código de verificación
     * @return Token si existe y no ha sido usado
     */
    Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);
    
    /**
     * Busca un token activo por email del usuario
     * Útil para invalidar tokens anteriores del mismo usuario
     * @param email Email del usuario
     * @return Token activo si existe
     */
    Optional<PasswordResetToken> findByEmailAndUsedFalse(String email);
    
    /**
     * Elimina tokens expirados (aunque MongoDB TTL se encarga automáticamente)
     * Método de respaldo para limpieza manual si es necesario
     * @param date Fecha límite para eliminar tokens anteriores
     */
    void deleteByExpirationDateBefore(LocalDateTime date);
}