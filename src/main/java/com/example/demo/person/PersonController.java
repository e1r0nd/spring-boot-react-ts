package com.example.demo.person;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    @Autowired
    PersonRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getAllPersons(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Person> pagedResult = repository.findAll(paging);

        List<Person> list;
        if (pagedResult.hasContent()) {
            list = pagedResult.getContent();
        } else {
            list = new ArrayList<>();
        }

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/")
    public String create(@RequestParam(value = "firstName", defaultValue = "firstName") String firstName,
            @RequestParam(value = "lastName", defaultValue = "lastName") String lastName) {
        Person userName = new Person(firstName, lastName);
        repository.save(userName);

        Iterable<Person> persons = repository.findAll();
        return persons.toString();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deleteById(@PathVariable("id") Long id) {
        Optional<Person> person = repository.findById(id);
        if (!person.isPresent()) {
            return new ResponseEntity<>(new Person(String.valueOf(id), ""), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        repository.deleteById(id);
        return new ResponseEntity<>(person.get(), new HttpHeaders(), HttpStatus.OK);
    }
}
