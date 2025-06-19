package edu.humboldt.usuarios.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio encargado del envío de emails para recuperación de contraseña
 * Utiliza JavaMailSender para enviar emails HTML con el código de verificación
 */
@Service
@RequiredArgsConstructor // Genera constructor con campos final/NonNull
@Slf4j // Genera logger automáticamente
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("Gestión de horarios humboldt - Ing Soft")
    private String appName;
    
    /**
     * Envía un email con el código de recuperación de contraseña
     * @param email Destinatario del email
     * @param code Código de verificación de 6 dígitos
     * @throws RuntimeException si hay error enviando el email
     */
    public void sendPasswordResetCode(String email, String code) {
        try {
            log.info("Enviando código de recuperación a: {}", email);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Código de Recuperación de Contraseña - " + appName);
            
            String htmlContent = buildEmailContent(code);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Email enviado exitosamente a: {}", email);
            
        } catch (MessagingException e) {
            log.error("Error enviando email a {}: {}", email, e.getMessage());
            throw new RuntimeException("Error enviando email de recuperación: " + e.getMessage());
        }
    }
    
    /**
     * Construye el contenido HTML del email con el código de verificación
     * @param code Código de verificación
     * @return HTML del email formateado
     */
    private String buildEmailContent(String code) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5;">
                <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #333; text-align: center; margin-bottom: 30px;">Recuperación de Contraseña</h2>
                    
                    <p style="color: #666; font-size: 16px; line-height: 1.5;">
                        Has solicitado restablecer tu contraseña en <strong>%s</strong>.
                    </p>
                    
                    <div style="background-color: #f8f9fa; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0;">
                        <p style="color: #333; font-size: 14px; margin-bottom: 10px;">Tu código de verificación es:</p>
                        <h1 style="color: #007bff; font-size: 32px; letter-spacing: 5px; margin: 10px 0;">%s</h1>
                    </div>
                    
                    <p style="color: #666; font-size: 14px; line-height: 1.5;">
                        <strong>⚠️ Este código expira en 15 minutos.</strong>
                    </p>
                    
                    <p style="color: #666; font-size: 14px; line-height: 1.5;">
                        Si no solicitaste este cambio, puedes ignorar este correo de forma segura.
                        Tu contraseña no será cambiada sin el código de verificación.
                    </p>
                    
                    <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                    
                    <p style="color: #999; font-size: 12px; text-align: center;">
                        Este es un mensaje automático, por favor no respondas a este correo.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(appName, code);
    }
}
