package edu.humboldt.usuarios.Demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Demo", description = "Controller for Demo")
public class DemoController {
    
    @PostMapping(value = "demo")
    public String welcome()
    {
        return "Welcome from secure endpoint";
    }
}