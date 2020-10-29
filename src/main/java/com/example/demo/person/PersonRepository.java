package com.example.demo.person;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
    @Override
    @PreAuthorize("#person?.manager == null or #person?.manager?.name == authentication?.name")
    <P extends Person> P save(@Param("person") P person);

    @Override
    @PreAuthorize("#person?.manager?.name == authentication?.name")
    void delete(@Param("person") Person person);
}