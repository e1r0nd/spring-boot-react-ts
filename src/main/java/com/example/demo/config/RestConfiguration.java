package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

import com.example.demo.person.Person;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/rest");
        config.exposeIdsFor(Person.class);

        config.exposeIdsFor(
                entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
    }
}