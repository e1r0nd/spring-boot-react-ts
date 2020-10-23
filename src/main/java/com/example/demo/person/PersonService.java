package com.example.demo.person;

import java.util.Map;
import java.util.Optional;

import com.example.demo.utilities.ErrorMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    PersonRepository repository;

    public ResponseEntity<Object> queryPersonById(Long id) {
        Optional<Person> person = repository.findById(id);
        if (!person.isPresent()) {
            Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.NOT_FOUND.value(),
                    String.format("No object found with id: %d", id));

            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(person.get(), new HttpHeaders(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addOrUpdatePerson(Map<String, String> person) {
        String id = person.get("id");
        String firstName = person.get("firstName");
        String lastName = person.get("lastName");

        if (firstName == null || lastName == null) {
            Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.BAD_REQUEST.value(),
                    "Required parameters were not provided");

            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        Person userName = new Person(firstName, lastName);
        Person newPerson = repository.save(userName);

        return new ResponseEntity<>(newPerson, new HttpHeaders(), HttpStatus.CREATED);
    }

}
