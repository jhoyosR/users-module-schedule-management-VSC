package edu.humboldt.usuarios.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/encripted/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para permitir peticiones desde el front
public class AuthAdminController {
    
    private final AuthAdminService authAdminService;
    
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authAdminService.login(request));
    }

}
