package com.psu.repository;


import com.psu.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // No need to write any CRUD operations, findAll(), findById(), save(), deleteById() are provided automatically.
    // You can define custom query methods here if needed.
}
