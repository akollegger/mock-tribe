package com.theice.tribe.service;

import com.theice.tribe.domain.Market;
import com.theice.tribe.domain.School;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends GraphRepository<School> {
}
