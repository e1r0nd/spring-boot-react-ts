package com.example.demo.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class ManagerService implements UserDetailsService {

    private final ManagerRepository repository;

    @Autowired
    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    // public UserDetails loadUserByUsername(String name) throws
    // UsernameNotFoundException { <- sonarlint(java:S1130)
    // Remove the declaration of thrown exception
    // 'org.springframework.security.core.userdetails.UsernameNotFoundException'
    // which is a runtime exception.
    public UserDetails loadUserByUsername(String name) {
        Manager manager = this.repository.findByName(name);
        return new User(manager.getName(), manager.getPassword(),
                AuthorityUtils.createAuthorityList(manager.getRoles()));
    }

}