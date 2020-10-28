package com.example.demo.account;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import com.example.demo.utilities.HasRole;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @GetMapping("/operations")
    public ResponseEntity<HashMap<String, Boolean>> getAvailableOperations() {
        HashMap<String, Boolean> result = new HashMap<>();
        boolean isManager = HasRole.hasRole("ROLE_MANAGER");
        result.put("hello", true);
        result.put("joke", true);
        result.put("secret", isManager);

        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<HashMap<String, Object>> getUserStatus() {
        HashMap<String, Object> result = new HashMap<>();
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = context.isAuthenticated() && !(context instanceof AnonymousAuthenticationToken);
        result.put("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            result.put("username", context.getName());
            result.put("roles", context.getAuthorities());
        }

        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

}
