package com.example.demo.person;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "person", types = Person.class)
public interface PersonProjection {
    @Value("#{target.id}")
    long getId();
}
