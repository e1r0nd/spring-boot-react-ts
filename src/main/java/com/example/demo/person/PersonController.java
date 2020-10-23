package com.example.demo.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    @Autowired
    PersonRepository repository;

    @Autowired
    PersonService service;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/")
    public ResponseEntity<List<Person>> getAllPersons(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Person> pagedResult = repository.findAll(paging);
        List<Person> list = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPersonById(@PathVariable("id") Long id) {
        return service.queryPersonById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePersonById(@PathVariable("id") Long id) {
        ResponseEntity<Object> result = service.queryPersonById(id);
        if (result.getStatusCode() == HttpStatus.OK) {
            repository.deleteById(id);
        }

        return result;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> createPerson(@RequestParam Map<String, String> person) {
        logger.info("Request for creation: {}", person);

        return service.addPerson(person);
    }

    @PutMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> patchPerson(@RequestParam Map<String, String> person) {
        logger.info("Request for update: {}", person);

        return service.updatePerson(person);
    }
}
