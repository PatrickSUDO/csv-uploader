package com.psu.controller;

import com.psu.model.Person;
import com.psu.service.PersonService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getAllPersons() {
        log.info("Requested all persons");
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        log.info("Requested person with id: {}", id);
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        log.info("Creating a new person with name: {}", person.getName());
        return personService.save(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        log.info("Updating person with id: {}", id);
        return personService.findById(id)
                .map(person -> {
                    person.setName(personDetails.getName());
                    person.setEmail(personDetails.getEmail());
                    Person updatedPerson = personService.save(person);
                    log.info("Updated person with id: {}", id);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        log.info("Deleting person with id: {}", id);
        return personService.findById(id)
                .map(person -> {
                    personService.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
