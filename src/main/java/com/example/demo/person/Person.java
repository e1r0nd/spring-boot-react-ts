package com.example.demo.person;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.demo.manager.Manager;

@Entity
public class Person {
    private @Id @GeneratedValue @Getter Long id;
    private @Getter @Setter String firstName;
    private @Getter @Setter String lastName;
    private @Getter @Setter @ManyToOne Manager manager;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.manager = null;
    }

    public Person(String firstName, String lastName, Manager manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.manager = manager;
    }
}