package com.example.demo.secret;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/secret")
@RestController
public class SecretController {
    @GetMapping("/")
    public String getSecret() {
        return "These data are available for managers only.";
    }
}
