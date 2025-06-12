package edu.humboldt.usuarios.Controller;

import edu.humboldt.usuarios.Service.PasswordResetService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService resetService;

    @PostMapping("/request-reset")
    public ResponseEntity<Void> requestReset(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        resetService.requestPasswordReset(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody Map<String,String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");
        resetService.resetPassword(token, newPassword, confirmPassword);
        return ResponseEntity.noContent().build();
    }
}

