package com.example.demo.account;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccountHelper {
    private AccountHelper() {
    }

    public static boolean hasRole(String role) {
        boolean hasRole = false;
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        if (context.isAuthenticated()) {
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) context.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                hasRole = authority.getAuthority().equals(role);
                if (hasRole) {
                    break;
                }
            }
        }
        return hasRole;
    }

    public static String getName() {
        String name = "";

        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        if (context.isAuthenticated()) {
            name = context.getName();
        }

        return name;
    }
}
