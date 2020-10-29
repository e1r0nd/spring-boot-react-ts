package com.example.demo.secret;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/secret")
@RestController
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class SecretController {
    @GetMapping("/")
    public String getSecret() {
        return "These data are available for managers only.";
    }
}
