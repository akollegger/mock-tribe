package com.theice.tribe.service;

import com.theice.tribe.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GraphRepository<Person> {
}
