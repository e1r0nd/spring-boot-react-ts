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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.account.AccountHelper;
import com.example.demo.manager.Manager;
import com.example.demo.utilities.ErrorMessage;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    @Autowired
    PersonRepository repository;

    @Autowired
    PersonService service;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @GetMapping("/")
    public ResponseEntity<HashMap<String, Object>> getAllPersons(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Person> pagedResult = repository.findAll(paging);
        List<Person> list = pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();

        HashMap<String, Object> result = new HashMap<>();
        List<Long> editable = new ArrayList<>();

        for (Person person : list) {
            Manager manager = person.getManager();
            if (manager == null && AccountHelper.getName().equals("anonymousUser")) {
                editable.add(person.getId());
            }
            if (manager != null && manager.getName().equals(AccountHelper.getName())) {
                editable.add(person.getId());
            }
        }
        result.put("editable", editable);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("totalPages", pagedResult.getTotalPages());
        result.put("hasPrevious", pagedResult.hasPrevious());
        result.put("hasNext", pagedResult.hasNext());
        result.put("sortBy", sortBy);
        result.put("data", list);

        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPersonById(@PathVariable("id") Long id) {
        return service.queryPersonById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePersonById(@PathVariable("id") Long id) {
        Optional<Person> person = repository.findById(id);
        if (person.isPresent()) {
            Person personEntity = person.get();
            Manager manager = personEntity.getManager();

            if ((manager == null && AccountHelper.getName().equals("anonymousUser"))
                    || (manager != null && manager.getName().equals(AccountHelper.getName()))) {
                repository.deleteById(id);

                return new ResponseEntity<>(personEntity, new HttpHeaders(), HttpStatus.OK);
            } else {
                Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.NOT_FOUND.value(),
                        String.format("Object protected with id: %d", id));

                return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }
        Map<String, String> errorMessage = ErrorMessage.errorMessage(HttpStatus.NOT_FOUND.value(),
                String.format("No object found with id: %d", id));

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
