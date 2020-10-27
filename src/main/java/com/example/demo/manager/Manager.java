package com.example.demo.manager;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Manager {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private @Id @GeneratedValue @Getter Long id;
    private @Getter @Setter String name;
    private @Getter @JsonIgnore String password;
    private @Getter @Setter String[] roles;

    protected Manager() {
    }

    public Manager(String name, String password, String... roles) {
        this.name = name;
        this.setPassword(password);
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
