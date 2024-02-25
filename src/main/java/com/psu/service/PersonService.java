package com.psu.service;

import com.psu.model.Person;
import com.psu.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> findAll() {
        log.info("Fetching all persons");
        return personRepository.findAll();
    }

    public Optional<Person> findById(Long id) {
        log.info("Fetching person with id: {}", id);
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        log.info("Saving a new person: {}", person.getName());
        return personRepository.save(person);
    }

    public void deleteById(Long id) {
        log.info("Deleting person with id: {}", id);
        personRepository.deleteById(id);
    }
}
