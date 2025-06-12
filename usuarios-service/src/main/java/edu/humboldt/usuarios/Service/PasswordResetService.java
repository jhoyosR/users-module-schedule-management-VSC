package edu.humboldt.usuarios.Service;

import edu.humboldt.usuarios.Entities.PasswordResetToken;
import edu.humboldt.usuarios.Entities.User;
import edu.humboldt.usuarios.Repository.PasswordResetTokenRepository;
import edu.humboldt.usuarios.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private String generateToken() {
        byte[] random = new byte[24];
        new SecureRandom().nextBytes(random);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(random);
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        // elimina tokens previos
        passwordResetTokenRepository.deleteByUserId(user.getId());
        // genera y guarda uno nuevo
        String code = generateToken();
        PasswordResetToken prt = new PasswordResetToken(user.getId(), code);
        passwordResetTokenRepository.save(prt);

        // envía correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Código para recuperar contraseña");
        message.setText("Tu código de recuperación es: " + code + 
                        "\nEste código expirará en 15 minutos.");
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido o expirado"));
        User user = userRepository.findById(prt.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        // actualiza contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // elimina el token usado
        passwordResetTokenRepository.delete(prt);
    }
}

