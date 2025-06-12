package edu.humboldt.usuarios.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String userId;      // referencia al User.id

    private String token;       // código aleatorio

    @Indexed(expireAfterSeconds = 900)   
    private Instant createdAt;  // expirará automáticamente tras 15 min

    public PasswordResetToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
        this.createdAt = Instant.now();
    }
}

