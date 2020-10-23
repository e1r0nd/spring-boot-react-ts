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

    public ResponseEntity<Object> addPerson(Map<String, String> person) {
        String firstName = person.get("firstName");
        String lastName = person.get("lastName");

        if (firstName == null || lastName == null) {
            Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.BAD_REQUEST.value(),
                    "Required parameters were not provided");

            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        Person personEntity = new Person(firstName.trim(), lastName.trim());
        Person newPerson = repository.save(personEntity);

        return new ResponseEntity<>(newPerson, new HttpHeaders(), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updatePerson(Map<String, String> person) {
        Long id = person.get("id") == null ? 0 : Long.valueOf(person.get("id"));
        String firstName = person.get("firstName");
        String lastName = person.get("lastName");
        Optional<Person> personOptional = repository.findById(id);

        if (firstName == null || lastName == null || !personOptional.isPresent()) {
            Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.BAD_REQUEST.value(),
                    "Required parameters were not provided");

            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        Person personEntity = personOptional.get();
        personEntity.setFirstName(firstName.trim());
        personEntity.setLastName(lastName.trim());
        Person newPerson = repository.save(personEntity);

        return new ResponseEntity<>(newPerson, new HttpHeaders(), HttpStatus.OK);
    }

}
