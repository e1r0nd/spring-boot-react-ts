package com.example.demo.person;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
    @Override
    @PreAuthorize("#person?.manager == null or #person?.manager?.name == authentication?.name")
    // Person save(@Param("person") Person person); // <- Type safety
    // The return type Person for save(Person) from the type PersonRepository
    // needs unchecked conversion to conform to S from the type CrudRepository<T,ID>
    // Java(67109423)
    <P extends Person> P save(@Param("person") P person); // This should fix it

    @Override
    @PreAuthorize("@personRepository.findById(#id)?.manager?.name == authentication?.name")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#person?.manager?.name == authentication?.name")
    void delete(@Param("person") Person person);
}