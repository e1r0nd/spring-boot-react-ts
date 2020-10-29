package com.example.demo.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ManagerService implements UserDetailsService {

    private final ManagerRepository repository;

    @Autowired
    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        Manager manager = this.repository.findByName(name);
        if (manager == null) {
            throw new UsernameNotFoundException("User not authorized.");
        }
        return new User(manager.getName(), manager.getPassword(),
                AuthorityUtils.createAuthorityList(manager.getRoles()));
    }

}