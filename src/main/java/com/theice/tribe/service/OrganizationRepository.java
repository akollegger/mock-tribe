package com.theice.tribe.service;

import com.theice.tribe.domain.Organization;
import com.theice.tribe.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends GraphRepository<Organization> {
}
