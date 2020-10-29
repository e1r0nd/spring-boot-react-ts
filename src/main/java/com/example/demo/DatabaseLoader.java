package com.example.demo;

import com.example.demo.manager.*;
import com.example.demo.person.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Component
public class DatabaseLoader implements CommandLineRunner {

	private final PersonRepository persons;
	private final ManagerRepository managers;

	@Autowired
	public DatabaseLoader(PersonRepository personRepository, ManagerRepository managerRepository) {
		this.persons = personRepository;
		this.managers = managerRepository;
	}

	@Override
	public void run(String... strings) throws Exception {

		this.managers.save(new Manager("user", "password", "ROLE_USER"));
		Manager manager = this.managers.save(new Manager("manager", "password", "ROLE_MANAGER"));

		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("manager",
				"password", AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		this.persons.save(new Person("Frodo", "Doe"));
		this.persons.save(new Person("Bilbo", "Jackson"));
		this.persons.save(new Person("Captain", "Morgan"));

		this.persons.save(new Person("John", "Smith", manager));
		this.persons.save(new Person("Merry", "Brand", manager));
		this.persons.save(new Person("Pier", "Took", manager));

		SecurityContextHolder.clearContext();
	}
}
